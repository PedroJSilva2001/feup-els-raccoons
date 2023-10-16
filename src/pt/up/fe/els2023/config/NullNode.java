package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

public record NullNode() implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
