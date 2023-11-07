package pt.up.fe.els2023.table.schema;

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

    public static ListNode list(SchemaNode... nodes) {
        return new ListNode(List.of(nodes));
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
