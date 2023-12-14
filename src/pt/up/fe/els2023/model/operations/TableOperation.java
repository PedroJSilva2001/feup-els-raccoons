package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonGroupTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TableOperation {
    public abstract String getName();

    public abstract boolean isTerminal();

    public OperationResult execute(RacoonGroupTable table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        if (isTerminal()) {
            return OperationResult.ofGroupTable(table);
        }

        List<Table> tables = table.getGroups();

        if (tables.isEmpty()) {
            return OperationResult.ofGroupTable(table);
        }

        List<Table> newTables = new ArrayList<>();

        for (Table t : tables) {
            newTables.add(execute(t).getTable());
        }

        return OperationResult.ofGroupTable(new RacoonGroupTable(newTables));
    }

    public abstract OperationResult execute(Table table) throws ColumnNotFoundException, ImproperTerminalOperationException;

    protected List<Value> getColumnWithCommonNumberRep(Table table, String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }

        var commonNumberRep = col.getMostGeneralNumberRep();

        if (commonNumberRep == null) {
            // Column has no numbers
            return Collections.emptyList();
        }

        // Aggregate statistics ignore values other than numbers
        return col.getEntries().stream().filter(Value::isNumber).map(commonNumberRep::cast).collect(Collectors.toList());
    }
}
