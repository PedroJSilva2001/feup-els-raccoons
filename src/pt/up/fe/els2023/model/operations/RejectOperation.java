package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;

import java.util.ArrayList;
import java.util.List;

public class RejectOperation extends TableOperation {

    private final List<String> columns;

    public RejectOperation(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getName() {
        return "Reject( " + String.join(", ", columns) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }


    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        for (var column : columns) {
            if (!table.containsColumn(column)) {
                throw new ColumnNotFoundException(column);
            }
        }

        var columnsToKeep = new ArrayList<String>();

        for (var column : table.getColumns()) {
            if (!columns.contains(column.getName())) {
                columnsToKeep.add(column.getName());
            }
        }

        return new SelectOperation(columnsToKeep).execute(table);
    }
}
