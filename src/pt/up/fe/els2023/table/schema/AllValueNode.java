package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches all value properties in the object.
 */
public record AllValueNode(String format) implements SchemaNode {
    public AllValueNode() {
        this("%s");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
