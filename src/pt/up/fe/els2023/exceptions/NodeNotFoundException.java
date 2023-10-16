package pt.up.fe.els2023.exceptions;

public class NodeNotFoundException extends NodeTraversalException {
    public NodeNotFoundException(String property) {
        super(String.format("Node %s was not found", property));
    }
}
