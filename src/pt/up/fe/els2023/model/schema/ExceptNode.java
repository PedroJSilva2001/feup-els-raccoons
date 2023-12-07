package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

import java.util.Set;

/**
 * This node matches all properties except the ones specified.
 *
 * @param except The properties to exclude.
 */
public record ExceptNode(Set<String> except, String format) implements SchemaNode {
    public ExceptNode(Set<String> except) {
        this(except, "%s");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
