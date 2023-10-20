package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * The NullNode maps an object property to a table column with an identical name.
 */
public record NullNode() implements SchemaNode {
    public static NullNode nullNode() {
        return new NullNode();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
