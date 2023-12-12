package pt.up.fe.els2023.model.schema;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * This node matches a specific index of an array, or a specific child of an object.
 *
 * @param index The position within the array or the index of the child within the object.
 * @param value The schema definition for the value found at the specified index or child.
 */
public record IndexNode(int index, SchemaNode value) implements SchemaNode {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
