package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node matches the file being imported.
 *
 * @param columnName The name of the column to store the file in.
 */
public record FileNode(String columnName) implements SchemaNode {
    public FileNode() {
        this("$file");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
