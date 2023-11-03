package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;


/**
 * This node matches a child with a specific name within an object.
 * <p>
 * Useful only when importing XML files. If used with JSON or YAML, it will default to the IndexNode implementation.
 *
 * @param index   The position within the array or the index of the child within the object.
 * @param keyName The name of the child.
 * @param value   The schema definition for the value found at the specified index or child.
 */
public record IndexOfNode(int index, String keyName, SchemaNode value) implements SchemaNode {
    public static IndexOfNode indexOf(int index, String keyName, SchemaNode value) {
        return new IndexOfNode(index, keyName, value);
    }

    public static IndexOfNode indexOf(int index, String keyName, SchemaNode... values) {
        return new IndexOfNode(index, keyName, new ListNode(values));
    }

    public static IndexOfNode indexOf(int index, String keyName, String columnName) {
        return new IndexOfNode(index, keyName, new ColumnNode(columnName));
    }

    public static IndexOfNode indexOf(int index, String keyName) {
        return new IndexOfNode(index, keyName, new NullNode());
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
