package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node defines a column within the table to which the object will be mapped.
 *
 * @param columnName The name assigned to the column
 */
public record ColumnNode(String columnName) implements SchemaNode {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
