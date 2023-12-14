package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node matches the directory of the file being imported.
 *
 * @param columnName The name of the column to store the directory in.
 */
public record DirectoryNode(String columnName) implements SchemaNode {
    public DirectoryNode() {
        this("$directory");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
