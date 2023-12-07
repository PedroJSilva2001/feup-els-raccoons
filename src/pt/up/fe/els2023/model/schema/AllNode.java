package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all properties in the object.
 */
public record AllNode(String format) implements SchemaNode {
    public AllNode() {
        this("%s");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
