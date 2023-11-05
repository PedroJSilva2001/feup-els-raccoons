package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node defines a column within the table to which the object will be mapped.
 *
 * @param columnName The name assigned to the column
 */
public record ColumnNode(String columnName) implements SchemaNode {
    public static ColumnNode column(String columnName) {
        return new ColumnNode(columnName);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
