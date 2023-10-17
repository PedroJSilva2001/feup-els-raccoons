package pt.up.fe.els2023.imports;

import java.util.LinkedList;
import java.util.List;

public class IntermediateTable {
    private final List<String> columnNames = new LinkedList<>();
    private final List<List<Object>> rows = new LinkedList<>();

    public boolean hasColumn(String columnName) {
        return columnNames.contains(columnName);
    }

    public int addColumn(String columnName) {
        if (hasColumn(columnName)) {
            return -1;
        }

        columnNames.add(columnName);

        for (List<Object> row : rows) {
            row.add(null);
        }

        return columnNames.size() - 1;
    }

    public int addColumnAfter(String columnName, String before) {
        if (hasColumn(columnName)) {
            return -1;
        }

        int index = columnNames.indexOf(before);
        boolean isLast = index == columnNames.size() - 1 || index == -1;

        if (isLast) {
            columnNames.add(columnName);
        } else {
            columnNames.add(index + 1, columnName);
        }

        if (isLast) {
            for (List<Object> row : rows) {
                row.add(null);
            }
        } else {
            for (List<Object> row : rows) {
                row.add(index + 1, null);
            }
        }

        return index + 1;
    }

    public void addRow(List<Object> values) {
        while (values.size() < columnNames.size()) {
            values.add(null);
        }

        rows.add(values);
    }
}
