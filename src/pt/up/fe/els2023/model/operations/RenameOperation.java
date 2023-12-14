package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

public class RenameOperation extends TableOperation {
    private final String oldColumnName;
    private final String newColumnName;

    public RenameOperation(String oldColumnName, String newColumnName) {
        this.oldColumnName = oldColumnName;
        this.newColumnName = newColumnName;
    }

    @Override
    public String getName() {
        return "Rename( " + oldColumnName + " -> " + newColumnName + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        Table newTable = new RacoonTable();

        if (!table.getColumNames().contains(oldColumnName)) {
            throw new ColumnNotFoundException(oldColumnName);
        }

        for (var columnName : table.getColumNames()) {
            if (columnName.equals(oldColumnName)) {
                newTable.addColumn(newColumnName);
            } else {
                newTable.addColumn(columnName);
            }
        }

        for (var row : table.getRows()) {
            newTable.addRow(row.getValues());
        }

        return OperationResult.ofTable(newTable);
    }
}
