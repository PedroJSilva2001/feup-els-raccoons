package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

public record AllContainerNode() implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
