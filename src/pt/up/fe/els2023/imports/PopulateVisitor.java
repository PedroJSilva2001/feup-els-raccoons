package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.exceptions.NodeNotAnObjectException;
import pt.up.fe.els2023.exceptions.NodeNotFoundException;
import pt.up.fe.els2023.utils.resources.ResourceNode;

import java.util.*;


public class PopulateVisitor implements NodeVisitor {
    private final Stack<TraversingInfo> traversingStack;
    private final NodeColumnMap nodeColumnMap;
    private final Map<String, Object> rowValues = new HashMap<>();
    private final Map<String, List<Object>> columnValues = new HashMap<>();

    private PopulateVisitor(NodeColumnMap nodeColumnMap, Stack<TraversingInfo> traversingStack) {
        this.nodeColumnMap = nodeColumnMap;
        this.traversingStack = traversingStack;
    }

    public PopulateVisitor(NodeColumnMap nodeColumnMap) {
        this.nodeColumnMap = nodeColumnMap;
        this.traversingStack = new Stack<>();
    }

    public PopulateVisitor() {
        this.nodeColumnMap = new NodeColumnMap();
        this.traversingStack = new Stack<>();
    }

    public Map<String, List<Object>> populateFromSource(ResourceNode rootNode, List<SchemaNode> schemaNodes) {
        this.rowValues.clear();
        this.columnValues.clear();
        this.traversingStack.clear();
        boolean emptyStack = this.traversingStack.empty();

        if (emptyStack) {
            this.traversingStack.push(new TraversingInfo(rootNode, "$root"));
        }

        for (var node : schemaNodes) {
            node.accept(this);
        }

        if (emptyStack) {
            this.traversingStack.pop();
        }
        return this.columnValues;
    }

    @Override
    public void visit(AllContainerNode node) {
        TraversingInfo info = this.traversingStack.peek();

        // TODO: Think about supporting arrays as well
        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children.entrySet()) {
            if (child.getValue().isContainer()) {
                String propertyName = child.getKey();
                String columnName = ColumnUtils.makeUnique(propertyName, properties);

                properties.add(columnName);
                columnName = nodeColumnMap.add(node, columnName);

                rowValues.put(columnName, child.getValue().asText());
            }
        }
    }

    @Override
    public void visit(AllNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children.entrySet()) {
            String propertyName = child.getKey();
            String columnName = ColumnUtils.makeUnique(propertyName, properties);

            properties.add(columnName);
            columnName = nodeColumnMap.add(node, columnName);

            rowValues.put(columnName, child.getValue().asText());
        }
    }

    @Override
    public void visit(AllValueNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children.entrySet()) {
            if (child.getValue().isValue()) {
                String propertyName = child.getKey();
                String columnName = ColumnUtils.makeUnique(propertyName, properties);

                properties.add(columnName);
                columnName = nodeColumnMap.add(node, columnName);

                rowValues.put(columnName, child.getValue().asText());
            }
        }
    }

    @Override
    public void visit(ChildNode node) {
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.keyName());

        if (child == null) {
            return;
        }

        this.traversingStack.push(new TraversingInfo(child, node.keyName()));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(ColumnNode node) {
        TraversingInfo info = this.traversingStack.peek();
        String columnName = nodeColumnMap.add(node, node.columnName());

        rowValues.put(columnName, info.node.asText());
    }

    @Override
    public void visit(EachNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isArray()) {
            return;
        }

        ResourceNode firstItem = info.node.get(0);

        traversingStack.push(new TraversingInfo(firstItem, info.property));

        PopulateVisitor visitor = new PopulateVisitor(nodeColumnMap, traversingStack);

        Map<String, List<Object>> concatenatedColumnValues = new HashMap<>();

        for (var resourceNode : info.node.getChildren().values()) {
            Map<String, List<Object>> columns = visitor.populateFromSource(resourceNode, List.of(node.value()));

            for (var column : columns.entrySet()) {
                concatenatedColumnValues.computeIfAbsent(column.getKey(), (key) -> new ArrayList<>()).addAll(column.getValue());
                concatenatedColumnValues.computeIfPresent(column.getKey(), (key, value) -> {
                    value.addAll(column.getValue());
                    return value;
                });
            }
        }

        traversingStack.pop();
    }

    @Override
    public void visit(ExceptNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children.entrySet()) {
            if (!node.except().contains(child.getKey())) {
                String propertyName = child.getKey();
                String columnName = ColumnUtils.makeUnique(propertyName, properties);

                properties.add(columnName);
                columnName = nodeColumnMap.add(node, columnName);

                rowValues.put(columnName, child.getValue().asText());
            }
        }
    }

    @Override
    public void visit(IndexNode node) {
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.index());

        if (child == null) {
            return;
        }

        this.traversingStack.push(new TraversingInfo(child, info.property + "[" + node.index() + "]"));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(ListNode node) {
        for (var childNode : node.list()) {
            childNode.accept(this);
        }
    }

    @Override
    public void visit(NullNode node) {
        TraversingInfo info = this.traversingStack.peek();

        String columnName = nodeColumnMap.add(node, info.property);
        rowValues.put(columnName, info.node.asText());
    }

    private record TraversingInfo(ResourceNode node, String property) {
    }
}
