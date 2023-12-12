package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

import java.util.Map;

public class RenameOperation extends TableOperation {

    private final Map<String, String> columnMapping;

    public RenameOperation(Map<String, String> columnMapping) {
        this.columnMapping = columnMapping;
    }

    @Override
    public String getName() {
        var name = new StringBuilder("Rename( ");

        for (var entry : columnMapping.entrySet()) {
            name.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(", ");
        }

        name.append(" )");

        return name.toString();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        Table newTable = new RacoonTable();

        for (var renameEntries : columnMapping.entrySet()) {
            var oldName = renameEntries.getKey();

            if (!table.getColumNames().contains(oldName)) {
                throw new ColumnNotFoundException(oldName);
            }
        }

        for (var columnName : table.getColumNames()) {
            newTable.addColumn(columnMapping.getOrDefault(columnName, columnName));
        }

        for (var row : table.getRows()) {
            newTable.addRow(row.getValues());
        }

        return OperationResult.ofTable(newTable);
    }
}
