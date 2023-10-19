package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.SchemaNode;
import pt.up.fe.els2023.utils.IdentityWrapper;

import java.util.*;

public class NodeColumnMap {
    private record NodeColumnPair(IdentityWrapper<SchemaNode> node, String columnName) {}
    private final Map<NodeColumnPair, String> nodeColumnMap = new LinkedHashMap<>();

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

    public String get(SchemaNode node) {
        return nodeColumnMap.get(node);
    }

    public List<String> getOrderedColumnNames(List<IdentityWrapper<SchemaNode>> nodes) {
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
            columnNames.addAll(nodeColumnMap.get(node));
        }

        return columnNames;
    }
}
