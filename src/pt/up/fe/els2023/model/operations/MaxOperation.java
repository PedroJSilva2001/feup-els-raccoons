package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaxOperation extends TerminalOperation {
    public MaxOperation(List<String> columnNames) {
        super(columnNames);
        assert(!columnNames.isEmpty());
    }

    @Override
    public String getName() {
        return "Max( " + String.join(", ", getColumns()) + " )";
    }

    // TODO dont forget ImproperTerminalOperationException
    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        var columnNames = getColumns();
        if (columnNames.size() == 1) {
            return OperationResult.ofValue(executeForSingleColumn(table, columnNames.get(0)));
        }

        return OperationResult.ofValueMap(executeForMultipleColumns(table, columnNames));
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

    private Map<String, Value> executeForMultipleColumns(Table table, List<String> columnNames) throws ColumnNotFoundException {
        var valueMap = new HashMap<String, Value>();

        for (var columnName : columnNames) {
            var value = executeForSingleColumn(table, columnName);
            valueMap.put(columnName, value);
        }

        return valueMap;
    }
}
