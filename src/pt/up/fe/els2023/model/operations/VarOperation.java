package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarOperation extends TableOperation {

    private final List<String> columnNames;

    public VarOperation(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String getName() {
        return "Var( " + String.join(", ", columnNames) + " )";
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

        if (colValues.isEmpty() || colValues.size() == 1) {
            return null;
        }

        var meanRes = new MeanOperation(List.of(columnName)).execute(table).getValue();

        if (meanRes == null) {
            return null;
        }

        Value.Type commonNumberRep;
        if (colValues.get(0).getType() == Value.Type.LONG) {
            commonNumberRep = Value.Type.DOUBLE;
        } else {
            commonNumberRep = colValues.get(0).getType();
        }

        var varRes = colValues.stream()
                .map(v -> commonNumberRep.cast(v).subtract(meanRes).pow(Value.of(2.0)))
                .reduce(commonNumberRep.additiveIdentity(), (v1, v2) -> Value.add(v1, v2))
                .divide(commonNumberRep.cast(Value.of(colValues.size())));

        return varRes;
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
