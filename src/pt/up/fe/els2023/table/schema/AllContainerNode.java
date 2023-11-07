package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all container properties in the object.
 */
public record AllContainerNode() implements SchemaNode {
    public static AllContainerNode allContainer() {
        return new AllContainerNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
