package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonGroupTable;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class GroupByOperation extends TableOperation {
    private final String columnName;

    public GroupByOperation(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getName() {
        return "GroupBy(" + columnName + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        return OperationResult.ofGroupTable(new RacoonGroupTable(table, columnName));
    }
}