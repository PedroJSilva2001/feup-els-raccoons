package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaxOperation extends TableOperation {

    private final List<String> columnNames;

    public MaxOperation(List<String> columnNames) {
        assert(!columnNames.isEmpty());

        this.columnNames = columnNames;
    }

    @Override
    public String getName() {
        return "Max( " + String.join(", ", columnNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    // TODO dont forget ImproperTerminalOperationException
    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        if (columnNames.size() == 1) {
            return OperationResult.ofValue(executeForSingleColumn(table, columnNames.get(0)));
        }

        return OperationResult.ofValueMap(executeForMultipleColumns(table));
    }

    private Value executeForSingleColumn(Table table, String columnName) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(table, columnName);

        if (colValues.isEmpty()) {
            return null;
        }

        var commonNumberRep = colValues.get(0).getType();

        var result = colValues.stream().max(commonNumberRep.comparator());

        return result.orElse(null);
    }

    private Map<String, Value> executeForMultipleColumns(Table table) throws ColumnNotFoundException {
        var valueMap = new HashMap<String, Value>();

        for (var columnName : columnNames) {
            var value = executeForSingleColumn(table, columnName);
            valueMap.put(columnName, value);
        }

        return valueMap;
    }
}
