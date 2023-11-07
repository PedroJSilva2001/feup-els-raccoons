package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * Represents a node in a schema for data mapping.
 */
public interface SchemaNode {
    void accept(NodeVisitor visitor);
}
