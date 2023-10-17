package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;
import pt.up.fe.els2023.exceptions.NodeTraversalException;

public record IndexNode(int index, String keyName, SchemaNode value) implements SchemaNode {
    public IndexNode(int index, SchemaNode value) {
        this(index, "", value);
    }

    @Override
    public void accept(NodeVisitor visitor) throws NodeTraversalException {
        visitor.visit(this);
    }
}
