package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeVisitor;

/**
 * A node that unwraps an array into multiple rows.
 * <p>
 * This allows you to create subdivisions within tables.
 * Each node is designed to iterate through a collection and extract specific elements,
 * converting them into separate rows within the resulting table structure.
 *
 * <p>
 * You can nest EachNodes to handle multi-level data structures
 * and create subdivisions within the table.
 *
 * @param value The schema of the values in the array.
 */
public record EachNode(SchemaNode value) implements SchemaNode {
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
