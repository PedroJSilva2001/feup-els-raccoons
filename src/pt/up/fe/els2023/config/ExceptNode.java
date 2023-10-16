package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;
import pt.up.fe.els2023.exceptions.NodeTraversalException;

import java.util.Set;

public record ExceptNode(Set<String> except) implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) throws NodeTraversalException {
        visitor.visit(this);
    }
}
