package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all container properties in the object.
 */
public record AllContainerNode(String format) implements SchemaNode {
    public AllContainerNode() {
        this("%s");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
