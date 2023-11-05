package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.Value;

import java.util.Map;

public class RowWrapper {
    private final Map<String, Value> rowValuesMapping;

    public RowWrapper(Map<String, Value> rowValuesMapping) {
        this.rowValuesMapping = rowValuesMapping;
    }

    public boolean contains(String column) {
        return rowValuesMapping.containsKey(column);
    }

    public Value get(String column) {
        return rowValuesMapping.get(column);
    }

    public Object getObject(String column) {
        return rowValuesMapping.get(column).getValue();
    }
}
