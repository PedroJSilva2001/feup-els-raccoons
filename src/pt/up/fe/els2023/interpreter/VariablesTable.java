package pt.up.fe.els2023.interpreter;

import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Value;

import java.util.HashMap;
import java.util.Map;

public class VariablesTable {
    private final Map<String, ITable> tables;
    private final Map<String, Value> values;

    public VariablesTable() {
        this.tables = new HashMap<>();
        this.values = new HashMap<>();
    }

    public VariablesTable(Map<String, ITable> tables, Map<String, Value> values) {
        this.tables = tables;
        this.values = values;
    }

    public boolean putTable(String name, ITable table) {
        if (tables.containsKey(name)) {
            return false;
        }

        tables.put(name, table);

        return true;
    }

    public boolean putValue(String name, Value value) {
        if (values.containsKey(name)) {
            return false;
        }

        values.put(name, value);

        return true;
    }

    public boolean hasTable(String name) {
        return tables.containsKey(name);
    }

    public boolean hasValue(String name) {
        return values.containsKey(name);
    }

    public ITable getTable(String name) {
        return tables.get(name);
    }

    public Value getValue(String name) {
        return values.get(name);
    }

    public Map<String, Value> getVariables() {
        return values;
    }
}
