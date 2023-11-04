package pt.up.fe.els2023.config;

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

    public static FileNode file(String columnName) {
        return new FileNode(columnName);
    }

    public static FileNode file() {
        return new FileNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
