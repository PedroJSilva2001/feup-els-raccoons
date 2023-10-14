package pt.up.fe.els2023.config;

import pt.up.fe.els2023.NodeVisitor;

import java.util.List;

public record NFTNode(List<SchemaNode> nodes) implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        // TODO
    }
}
