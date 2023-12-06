package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.RacoonTable;
import pt.up.fe.els2023.table.Table;

import java.util.List;

public class ArgMaxOperation extends TableOperation {
    private final String columnName;

    public ArgMaxOperation(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getName() {
        return "ArgMax(" + columnName + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        var maxResult = new MaxOperation(List.of(columnName)).execute(table);

        if (maxResult.getValue() == null) {
            return OperationResult.ofTable(new RacoonTable(table.getColumNames()));
        }

        return new WhereOperation(rowWrapper -> rowWrapper.get(columnName).equals(maxResult)).execute(table);
    }
}