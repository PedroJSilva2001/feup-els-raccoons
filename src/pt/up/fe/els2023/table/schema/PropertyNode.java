package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches a specific property in the object.
 *
 * @param keyName The name of the property.
 * @param value   The schema of the property.
 */
public record PropertyNode(String keyName, SchemaNode value) implements SchemaNode {
    public static PropertyNode property(String keyName, SchemaNode value) {
        return new PropertyNode(keyName, value);
    }

    public static PropertyNode property(String keyName, SchemaNode... values) {
        return new PropertyNode(keyName, new ListNode(values));
    }

    public static PropertyNode property(String keyName, String columnName) {
        return new PropertyNode(keyName, new ColumnNode(columnName));
    }

    public static PropertyNode property(String keyName) {
        return new PropertyNode(keyName, new NullNode());
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
