package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

import java.util.Set;

public record ExceptNode(Set<String> except) implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
