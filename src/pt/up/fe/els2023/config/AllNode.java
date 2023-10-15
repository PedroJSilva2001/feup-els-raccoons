package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

public record AllNode() implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        // TODO
    }
}
