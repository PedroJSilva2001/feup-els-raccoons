package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.exceptions.NodeNotAnObjectException;
import pt.up.fe.els2023.exceptions.NodeNotFoundException;
import pt.up.fe.els2023.exceptions.NodeTraversalException;
import pt.up.fe.els2023.utils.resources.ResourceNode;

import java.util.*;


public class ColumnVisitor implements NodeVisitor {
    private final Stack<TraversingInfo> traversingStack = new Stack<>();
    private final List<String> columnNames = new LinkedList<>();
    private final Map<String, Object> rowValues = new HashMap<>();
    private final Map<String, List<Object>> columnValues = new HashMap<>();

    public List<String> getColumnNames(ResourceNode rootNode, List<SchemaNode> schemaNodes) throws NodeTraversalException {
        this.columnNames.clear();
        this.traversingStack.clear();

        this.traversingStack.push(new TraversingInfo(rootNode, "$root"));

        for (var node : schemaNodes) {
            node.accept(this);
        }

        this.traversingStack.pop();

        return this.columnNames;
    }

    private void addColumn(String columnName) {
        String finalColumnName = columnName;
        long count = columnNames.stream().filter((name) -> name.equals(finalColumnName)).count();

        if (count != 0) {
            columnName += "_" + count;
        }

        columnNames.add(columnName);
    }

    @Override
    public void visit(AllContainerNode node) throws NodeNotAnObjectException {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            throw new NodeNotAnObjectException(info.property);
        }

        var children = info.node.getChildren();

        for (var child : children.entrySet()) {
            if (child.getValue().isContainer()) {
                addColumn(child.getKey());
            }
        }
    }

    @Override
    public void visit(AllNode node) throws NodeNotAnObjectException {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            throw new NodeNotAnObjectException(info.property);
        }

        var children = info.node.getChildren();

        for (var child : children.entrySet()) {
            addColumn(child.getKey());
        }
    }

    @Override
    public void visit(AllValueNode node) throws NodeNotAnObjectException {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            throw new NodeNotAnObjectException(info.property);
        }

        var children = info.node.getChildren();

        for (var child : children.entrySet()) {
            if (child.getValue().isValue()) {
                addColumn(child.getKey());
            }
        }
    }

    @Override
    public void visit(ChildNode node) throws NodeTraversalException {
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.keyName());

        if (child == null) {
            throw new NodeNotFoundException(node.keyName());
        }

        this.traversingStack.push(new TraversingInfo(child, node.keyName()));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(ColumnNode node) {
        addColumn(node.columnName());
    }

    @Override
    public void visit(EachNode node) throws NodeTraversalException {
        // Maybe we will need to check if all children have the required properties
        // But for now we will just assume they do
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isArray()) {
            return;
        }

        ResourceNode firstItem = info.node.get(0);

        traversingStack.push(new TraversingInfo(firstItem, info.property));

        node.value().accept(this);

        traversingStack.pop();
    }

    @Override
    public void visit(ExceptNode node) throws NodeNotAnObjectException {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            throw new NodeNotAnObjectException(info.property);
        }

        var children = info.node.getChildren();

        for (var child : children.entrySet()) {
            if (!node.except().contains(child.getKey())) {
                addColumn(child.getKey());
            }
        }
    }

    @Override
    public void visit(IndexNode node) throws NodeTraversalException {
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.index());

        if (child == null) {
            throw new NodeNotFoundException(String.valueOf(node.index()));
        }

        this.traversingStack.push(new TraversingInfo(child, info.property + "[" + node.index() + "]"));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(ListNode node) throws NodeTraversalException {
        for (var childNode : node.list()) {
            childNode.accept(this);
        }
    }

    @Override
    public void visit(NullNode node) {
        TraversingInfo info = this.traversingStack.peek();

        addColumn(info.property);
    }

    private record TraversingInfo(ResourceNode node, String property) {
    }
}
