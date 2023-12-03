package pt.up.fe.els2023.table.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

import java.util.List;

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
