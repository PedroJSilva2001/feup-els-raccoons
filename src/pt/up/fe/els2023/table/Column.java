package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Column implements Cloneable {
    private final String name;

    private final List<Value> entries;

    public Column(String name, List<Value> entries) {
        this.entries = new ArrayList<>();
        this.name = name;

        this.entries.addAll(entries);
    }

    // Copies entire column
    public Column(Column column) {
        this.name = String.valueOf(column.name);
        this.entries = new ArrayList<>();

        entries.addAll(column.entries);
    }

    public Column(String name) {
        this.entries = new ArrayList<>();
        this.name = name;
    }

    public void addEntry(Value value) {
        entries.add(value);
    }

    public String getName() {
        return name;
    }

    public List<Value> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
