package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

public interface SchemaNode {
    void accept(NodeVisitor visitor);
}
