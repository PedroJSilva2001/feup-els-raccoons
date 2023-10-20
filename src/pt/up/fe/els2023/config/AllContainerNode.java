package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all container properties in the object.
 */
public record AllContainerNode() implements SchemaNode {
    public static AllContainerNode allContainerNode() {
        return new AllContainerNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
