package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

public record IndexNode(int index, String keyName, SchemaNode value) implements SchemaNode {
    public IndexNode(int index, SchemaNode value) {
        this(index, "", value);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        // TODO
    }
}
