package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.utils.IdentityWrapper;

import java.util.LinkedList;
import java.util.List;

public class NodeOrderVisitor implements NodeVisitor {
    private final List<IdentityWrapper<SchemaNode>> nodes = new LinkedList<>();

    @Override
    public void visit(AllContainerNode node) {
        nodes.add(new IdentityWrapper<>(node));
    }

    @Override
    public void visit(AllNode node) {
        nodes.add(new IdentityWrapper<>(node));
    }

    @Override
    public void visit(AllValueNode node) {
        nodes.add(new IdentityWrapper<>(node));
    }

    @Override
    public void visit(PropertyNode node) {
        node.value().accept(this);
    }

    @Override
    public void visit(ColumnNode node) {
        nodes.add(new IdentityWrapper<>(node));
    }

    @Override
    public void visit(EachNode node) {
        node.value().accept(this);
    }

    @Override
    public void visit(ExceptNode node) {
        nodes.add(new IdentityWrapper<>(node));
    }

    @Override
    public void visit(IndexNode node) {
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
        nodes.add(new IdentityWrapper<>(node));
    }

    public List<IdentityWrapper<SchemaNode>> getNodeOrder(List<SchemaNode> nft) {
        nodes.clear();

        for (var node : nft) {
            node.accept(this);
        }

        return nodes;
    }
}
