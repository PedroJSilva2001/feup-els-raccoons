package pt.up.fe.els2023.operations;

import java.util.Map;

public class RowWrapper {
    private final Map<String, Object> rowValuesMapping;

    public RowWrapper(Map<String, Object> rowValuesMapping) {
        this.rowValuesMapping = rowValuesMapping;
    }

    public boolean contains(String column) {
        return rowValuesMapping.containsKey(column);
    }

    public Object get(String column) {
        return rowValuesMapping.get(column);
    }
}
