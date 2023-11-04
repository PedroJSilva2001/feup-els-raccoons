package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;

import java.util.LinkedList;
import java.util.List;

/**
 * This visitor visits all nodes in a schema and returns them in the order they were visited.
 */
public class NodeOrderVisitor implements NodeVisitor {
    private final List<SchemaNode> nodes = new LinkedList<>();

    @Override
    public void visit(AllContainerNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(AllNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(AllValueNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(PropertyNode node) {
        node.value().accept(this);
    }

    @Override
    public void visit(ColumnNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(EachNode node) {
        node.value().accept(this);
    }

    @Override
    public void visit(ExceptNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(IndexNode node) {
        node.value().accept(this);
    }

    @Override
    public void visit(IndexOfNode node) {
        node.value().accept(this);
    }

    @Override
    public void visit(ListNode node) {
        for (var child : node.list()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(NullNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(DirectoryNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(FileNode node) {
        nodes.add(node);
    }

    @Override
    public void visit(PathNode node) {
        nodes.add(node);
    }

    public List<SchemaNode> getNodeOrder(List<SchemaNode> nft) {
        nodes.clear();

        for (var node : nft) {
            node.accept(this);
        }

        return nodes;
    }
}
