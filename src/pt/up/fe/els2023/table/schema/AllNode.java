package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all properties in the object.
 */
public record AllNode() implements SchemaNode {
    public static AllNode all() {
        return new AllNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
