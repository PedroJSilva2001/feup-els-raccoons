package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;
import pt.up.fe.els2023.exceptions.NodeTraversalException;

public interface SchemaNode {
    void accept(NodeVisitor visitor);
}
