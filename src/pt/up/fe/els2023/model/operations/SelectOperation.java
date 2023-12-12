package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.ArrayList;
import java.util.List;

public class SelectOperation extends TableOperation {

    private final List<String> columns;

    public SelectOperation(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getName() {
        return "Select( " + String.join(", ", columns) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }


    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
         Table newTable = new RacoonTable();

        var columnsToKeep = new ArrayList<Integer>();

        for (var column : columns) {
            if (!table.containsColumn(column)) {
                throw new ColumnNotFoundException(column);
            }

            newTable.addColumn(column);

            columnsToKeep.add(table.getIndexOfColumn(column));
        }

        if (columnsToKeep.isEmpty()) {
            return OperationResult.ofTable(new RacoonTable(newTable.getColumNames()));
        }

        for (var row : table.getRows()) {
            List<Value> values = new ArrayList<>();

            for (var column : columnsToKeep) {
                values.add(row.get(column));
            }

            newTable.addRow(values);
        }

        return OperationResult.ofTable(newTable);
    }
}
