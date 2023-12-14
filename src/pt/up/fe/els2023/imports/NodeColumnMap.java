package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.model.schema.SchemaNode;

import java.util.*;

/**
 * The NodeColumnMap class is responsible for mapping SchemaNodes to table column names and
 * ensuring column name uniqueness.
 * <p>
 * This is used to ensure that the same column name is assigned to the same SchemaNode, and that no two SchemaNodes
 * are assigned the same column name. By doing this, consistent naming across multiple files of a source is ensured.
 * <p>
 * We can also derive the ordering of the columns from this map. Each SchemaNode can be assigned to multiple columns,
 * and the order of the columns in that SchemaNode is the order in which they are added to the map, or found in the
 * source file.
 */
public class NodeColumnMap {
    /**
     * The map that maps SchemaNodes to column names.
     * <p>
     * The reason why we map NodeColumnPairs instead of SchemaNodes is because SchemaNodes can be
     * assigned to multiple columns because of AllNode and its siblings.
     * <p>
     * This is a LinkedHashMap so that the order of the columns for each SchemaNode is preserved.
     * <p>
     * The value of the map is the unique column name assigned to the SchemaNodePair.
     */
    private final Map<NodeColumnPair, String> nodeColumnMap = new LinkedHashMap<>();

    /**
     * Adds a SchemaNode pairing to the map. If the pair is already in the map, then the previous column name
     * is returned.
     *
     * @param node       The SchemaNode
     * @param columnName The column name defined in the table schema, or found in the source file.
     * @return The unique column name assigned to the SchemaNodePair.
     */
    public String add(SchemaNode node, String columnName) {
        String previous = nodeColumnMap.get(new NodeColumnPair(new IdentityWrapper<>(node), columnName));
        if (previous != null) {
            return previous;
        }

        Set<String> columnNames = new HashSet<>(nodeColumnMap.values());

        String newColumnName = ColumnUtils.makeUnique(columnName, columnNames);

        nodeColumnMap.put(new NodeColumnPair(new IdentityWrapper<>(node), columnName), newColumnName);
        return newColumnName;
    }

    /**
     * Retrieves a list of column names in the order specified by a list of SchemaNodes. These can be generated
     * using the {@link NodeOrderVisitor} class.
     *
     * @param nodes The list of SchemaNodes that define the order of the columns.
     * @return The ordered list of column names.
     */
    public List<String> getOrderedColumnNames(List<SchemaNode> nodes) {
        List<String> columnNames = new ArrayList<>();
        Map<IdentityWrapper<SchemaNode>, LinkedHashSet<String>> nodeColumnMap = new HashMap<>();

        for (var pair : this.nodeColumnMap.entrySet()) {
            var node = pair.getKey().node();

            if (!nodeColumnMap.containsKey(node)) {
                nodeColumnMap.put(node, new LinkedHashSet<>());
            }

            nodeColumnMap.get(node).add(pair.getValue());
        }

        for (var node : nodes) {
            Set<String> columnNamesSet = nodeColumnMap.get(new IdentityWrapper<>(node));
            if (columnNamesSet == null) {
                continue;
            }

            columnNames.addAll(columnNamesSet);
        }

        return columnNames;
    }

    /**
     * This record is used as a key in the map, and represents a SchemaNode and the column name defined in the
     * table schema, or found in the source file.
     *
     * @param node       The SchemaNode
     * @param columnName The column name
     */
    private record NodeColumnPair(IdentityWrapper<SchemaNode> node, String columnName) {
    }

    /**
     * This class wraps a value and compares it by identity.
     *
     * @param value The value to wrap.
     * @param <T>   The type of the value.
     */
    private record IdentityWrapper<T>(T value) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdentityWrapper<?> that = (IdentityWrapper<?>) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(value);
        }
    }
}
