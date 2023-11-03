package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all value properties in the object.
 */
public record AllValueNode() implements SchemaNode {
    public static AllValueNode allValue() {
        return new AllValueNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
