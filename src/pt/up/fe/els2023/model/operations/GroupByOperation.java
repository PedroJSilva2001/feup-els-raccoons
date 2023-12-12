package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class GroupByOperation extends TableOperation {
    private final String columnName;
    private final TerminalOperation aggregateOperation;

    public GroupByOperation(String columnName, TerminalOperation aggregateOperation) {
        this.columnName = columnName;
        this.aggregateOperation = aggregateOperation;
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
        var set = new LinkedHashMap<Object, Table>();
        var rows = new RacoonTable(table.getColumNames());

        var columnIndex = table.getIndexOfColumn(columnName);
        for (var row : table.getRows()) {
            var value = row.get(columnIndex).getValue();
            if (!set.containsKey(value)) {
                set.put(value, new RacoonTable(table.getColumNames()));
            }
            var groupTable = set.get(value);
            groupTable.addRow(row.getValues());
        }

        for (var entry : set.entrySet()) {
            var groupTable = entry.getValue();

            if (aggregateOperation == null) {
                rows.addRow(groupTable.getRows().get(0).getValues());
            } else {
                var aggregate = aggregateOperation.execute(groupTable);
                var aggregateColumns = aggregateOperation.getColumns();

                if (aggregate.getType() == OperationResult.Type.VALUE) {
                    var column = aggregateColumns.get(0);

                    var operation = new WhereOperation(rowWrapper -> rowWrapper.get(column).equals(aggregate.getValue()));
                    var firstRow = operation.execute(groupTable).getTable().getRows().get(0);

                    rows.addRow(firstRow.getValues());
                }
            }
        }

        return OperationResult.ofTable(rows);
    }
}