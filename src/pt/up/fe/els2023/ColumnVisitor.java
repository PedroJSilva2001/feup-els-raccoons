package pt.up.fe.els2023;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.exceptions.NodeNotAnObjectException;
import pt.up.fe.els2023.exceptions.NodeNotFoundException;
import pt.up.fe.els2023.exceptions.NodeTraversalException;
import pt.up.fe.els2023.utils.resources.ResourceNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class ColumnVisitor implements NodeVisitor {
    private final ResourceNode rootNode;
    private final Stack<TraversingInfo> traversingStack = new Stack<>();
    private final List<String> columnNames = new ArrayList<>();

    public ColumnVisitor(ResourceNode rootNode) {
        this.rootNode = rootNode;
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
                this.columnNames.add(child.getKey());
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
            this.columnNames.add(child.getKey());
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
                this.columnNames.add(child.getKey());
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
        this.columnNames.add(node.columName());
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
                this.columnNames.add(child.getKey());
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

        this.columnNames.add(info.property);
    }

    public List<String> getColumnNames(List<SchemaNode> schemaNodes) throws NodeTraversalException {
        this.columnNames.clear();
        this.traversingStack.clear();

        this.traversingStack.push(new TraversingInfo(this.rootNode, "$root"));

        for (var node : schemaNodes) {
            node.accept(this);
        }

        this.traversingStack.pop();

        return this.columnNames;
    }

    private record TraversingInfo(ResourceNode node, String property) {
    }
}
