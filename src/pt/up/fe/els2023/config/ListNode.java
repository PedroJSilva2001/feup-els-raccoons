package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

import java.util.List;

public record ListNode(List<SchemaNode> list) implements SchemaNode {
    public ListNode(SchemaNode... nodes) {
        this(List.of(nodes));
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
