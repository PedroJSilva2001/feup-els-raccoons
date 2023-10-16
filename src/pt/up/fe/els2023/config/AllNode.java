package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;
import pt.up.fe.els2023.exceptions.NodeTraversalException;

public record AllNode() implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) throws NodeTraversalException {
        visitor.visit(this);
    }
}
