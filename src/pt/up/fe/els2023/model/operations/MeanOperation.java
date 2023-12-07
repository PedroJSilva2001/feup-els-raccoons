package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeanOperation extends TableOperation {

    private final List<String> columnNames;

    public MeanOperation(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String getName() {
        return "Mean( " + String.join(", ", columnNames) + " )";
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
        var colValues = getColumnWithCommonNumberRep(table, columnName);

        if (colValues.isEmpty()) {
            return null;
        }

        var sumRes = new SumOperation(List.of(columnName)).execute(table).getValue();

        if (sumRes == null) {
            return null;
        }

        var meanRes = sumRes.divide(sumRes.getType().cast(Value.of(colValues.size())));

        return meanRes;
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
