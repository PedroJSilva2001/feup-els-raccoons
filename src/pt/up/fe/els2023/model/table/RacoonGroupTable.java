package pt.up.fe.els2023.model.table;

import pt.up.fe.els2023.dsl.TableCascade;

import java.util.*;

public class RacoonGroupTable {
    private final List<Table> groups;

    public RacoonGroupTable(List<Table> groups) {
        this.groups = groups;
    }

    public RacoonGroupTable(Table table, String columnName) {
        var set = new LinkedHashMap<Object, Table>();

        var columnIndex = table.getIndexOfColumn(columnName);
        for (var row : table.getRows()) {
            var value = row.get(columnIndex).getValue();
            if (!set.containsKey(value)) {
                set.put(value, new RacoonTable(table.getColumNames()));
            }
            var groupTable = set.get(value);
            groupTable.addRow(row.getValues());
        }

        this.groups = new ArrayList<>();

        for (var entry : set.entrySet()) {
            var groupTable = entry.getValue();
            this.groups.add(groupTable);
        }
    }

    public List<Table> getGroups() {
        return groups;
    }

    public Table asTable() {
        List<String> columnNames = new ArrayList<>();

        if (!groups.isEmpty()) {
            columnNames = groups.get(0).getColumNames();
        }

        var table = new RacoonTable(columnNames);

        for (var group : groups) {
            for (var row : group.getRows()) {
                table.addRow(row.getValues());
            }
        }

        return table;
    }
}