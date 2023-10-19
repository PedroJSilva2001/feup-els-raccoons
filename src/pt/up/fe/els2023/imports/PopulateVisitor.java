package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;
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

        return merge();
    }

    private Map<String, List<Object>> merge() {
        int maxColumnSize = rowValues.values().stream().anyMatch(obj -> !Objects.isNull(obj)) ? 1 : 0;

        for (var column : columnValues.entrySet()) {
            maxColumnSize = Math.max(maxColumnSize, column.getValue().size());
        }

        Map<String, List<Object>> merged = new HashMap<>();

        for (var column : columnValues.entrySet()) {
            // Pad with nulls
            List<Object> values = new ArrayList<>(Collections.nCopies(maxColumnSize, null));

            for (int i = 0; i < column.getValue().size(); i++) {
                values.set(i, column.getValue().get(i));
            }

            merged.put(column.getKey(), values);
        }

        for (var row : rowValues.entrySet()) {
            merged.put(row.getKey(), new ArrayList<>(Collections.nCopies(maxColumnSize, row.getValue())));
        }

        return merged;
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

        for (var resourceNode : info.node) {
            traversingStack.push(new TraversingInfo(resourceNode, info.property));

            PopulateVisitor visitor = new PopulateVisitor(nodeColumnMap, traversingStack);
            Map<String, List<Object>> columns = visitor.populateFromSource(resourceNode, List.of(node.value()));

            for (var column : columns.entrySet()) {
                if (!columnValues.containsKey(column.getKey())) {
                    columnValues.put(column.getKey(), new ArrayList<>());
                }

                columnValues.get(column.getKey()).addAll(column.getValue());
            }

            traversingStack.pop();
        }
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
        // TODO: FORGOT TO IMPLEMENT CHILD WITH NAME
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
