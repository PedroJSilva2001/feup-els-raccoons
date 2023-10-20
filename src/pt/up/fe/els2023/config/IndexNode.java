package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node matches a specific index of an array, or a specific child of an object.
 *
 * @param index   The position within the array or the index of the child within the object.
 * @param value   The schema definition for the value found at the specified index or child.
 */
public record IndexNode(int index, SchemaNode value) implements SchemaNode {
    public static IndexNode index(int index, SchemaNode value) {
        return new IndexNode(index, value);
    }

    public static IndexNode index(int index, SchemaNode... values) {
        return new IndexNode(index, new ListNode(values));
    }

    public static IndexNode index(int index, String columnName) {
        return new IndexNode(index, new ColumnNode(columnName));
    }

    public static IndexNode index(int index) {
        return new IndexNode(index, new NullNode());
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
