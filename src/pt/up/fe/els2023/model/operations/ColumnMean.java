package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.List;

public class ColumnMean extends TableOperation{
    private final List<String> columnNames;

    public ColumnMean(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String getName() {
        return "ColumnMean(" + columnNames + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        var columns = columnNames.isEmpty() ? table.getColumNames() : columnNames;

        var newTable = new RacoonTable(columns);

        var sumResult = new MeanOperation(columns).execute(table);

        if (sumResult.getType() == OperationResult.Type.VALUE) {
            newTable.addRow(List.of(sumResult.getValue()));
        } else if (sumResult.getType() == OperationResult.Type.VALUE_MAP) {
            var map = sumResult.getValueMap();
            var row = new Value[columns.size()];

            for (var entry : map.entrySet()) {
                var columnIndex = table.getIndexOfColumn(entry.getKey());

                row[columnIndex] = entry.getValue();
            }

            newTable.addRow(List.of(row));
        }

        return OperationResult.ofTable(newTable);
    }
}
