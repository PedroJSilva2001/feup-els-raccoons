package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node matches a specific index of an array, or a specific child of an object.
 * <p>
 * When used with an object, the 'keyName' parameter is employed to retrieve the nth child with the specified name.
 * @param index The position within the array or the index of the child within the object.
 * @param keyName The name of the child within the object.
 * @param value The schema definition for the value found at the specified index or child.
 */
public record IndexNode(int index, String keyName, SchemaNode value) implements SchemaNode {
    public IndexNode(int index, SchemaNode value) {
        this(index, "", value);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
