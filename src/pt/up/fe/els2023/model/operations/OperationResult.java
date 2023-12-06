package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.Map;

public class OperationResult {
    public enum Type {
        TABLE,
        VALUE,
        VALUE_MAP
    }

    private final Table table;

    private final Value value;

    private final Map<String, Value> valueMap;

    private final Type type;

    private OperationResult(Table table, Value value, Map<String, Value> valueMap, Type type) {
        this.table = table;
        this.value = value;
        this.valueMap = valueMap;
        this.type = type;
    }

    public static OperationResult ofValue(Value value) {
        return new OperationResult(null, value, null, Type.VALUE);
    }

    public static OperationResult ofTable(Table table) {
        return new OperationResult(table, null, null, Type.TABLE);
    }

    public static OperationResult ofValueMap(Map<String, Value> valueMap) {
        return new OperationResult(null, null, valueMap, Type.VALUE_MAP);
    }

    public Value getValue() {
        return value;
    }

    public Table getTable() {
        return table;
    }

    public Map<String, Value> getValueMap() {
        return valueMap;
    }

    public Type getType() {
        return type;
    }
}
