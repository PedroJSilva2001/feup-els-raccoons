package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that matches a specific property in the object.
 *
 * @param keyName The name of the property.
 * @param value   The schema of the property.
 */
public record PropertyNode(String keyName, SchemaNode value) implements SchemaNode {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
