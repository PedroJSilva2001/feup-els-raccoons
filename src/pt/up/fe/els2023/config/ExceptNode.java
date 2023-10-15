package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

import java.util.List;

public record ExceptNode(List<String> except) implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {

    }
}
