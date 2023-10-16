package pt.up.fe.els2023.exceptions;

public class NodeNotAnObjectException extends NodeTraversalException {
    public NodeNotAnObjectException(String property) {
        super(String.format("Node %s is not an object", property));
    }
}
