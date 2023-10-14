package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

public record IndexNode(int index, String keyName, SchemaNode schemaNode) implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        // TODO
    }
}
