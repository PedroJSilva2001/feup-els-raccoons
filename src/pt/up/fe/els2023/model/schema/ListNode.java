package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

import java.util.List;

/**
 * This node represents a collection of nodes.
 *
 * @param list The list of nodes.
 */
public record ListNode(List<SchemaNode> list) implements SchemaNode {
    public ListNode(SchemaNode... nodes) {
        this(List.of(nodes));
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
