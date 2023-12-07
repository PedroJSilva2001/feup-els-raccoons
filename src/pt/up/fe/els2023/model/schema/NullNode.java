package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * The NullNode maps an object property to a table column with an identical name.
 */
public record NullNode() implements SchemaNode {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
