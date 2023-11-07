package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node matches the path of the file being imported.
 *
 * @param columnName The name of the column to store the path in.
 */
public record PathNode(String columnName) implements SchemaNode {
    public PathNode() {
        this("$path");
    }

    public static PathNode path(String columnName) {
        return new PathNode(columnName);
    }

    public static PathNode path() {
        return new PathNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
