package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

import java.util.Set;

/**
 * This node matches all properties except the ones specified.
 *
 * @param except The properties to exclude.
 */
public record ExceptNode(Set<String> except) implements SchemaNode {
    // TODO: Possibly except could be a SchemaNode, which would specify the schema of the properties to exclude.
    // TODO: This would be useful for specifying a certain index of an array, for example.
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
