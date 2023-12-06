package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountOperation extends TableOperation {

    private final List<String> columnNames;

    public CountOperation(List<String> columnNames) {
        assert(!columnNames.isEmpty());

        this.columnNames = columnNames;
    }

    @Override
    public String getName() {
        return "Count( " + String.join(", ", columnNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        if (columnNames.size() == 1) {
            return OperationResult.ofValue(executeForSingleColumn(table, columnNames.get(0)));
        }

        return OperationResult.ofValueMap(executeForMultipleColumns(table));
    }

    private Value executeForSingleColumn(Table table, String columnName) throws ColumnNotFoundException {
        var col = table.getColumn(columnName);

        if (col == null) {
            throw new ColumnNotFoundException(columnName);
        }

        var countVal = col.getEntries().stream().filter(
                (value) -> !value.isNull()
        ).count();

        return Value.of(countVal);
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
