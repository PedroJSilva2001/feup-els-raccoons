package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Column {
    private final String name;

    private final List<Value> entries;

    public Column(String name, List<Object> entries) {
        this.entries = new ArrayList<>();
        this.name = name;

        for (Object entry : entries) {
            this.entries.add(new Value(entry));
        }
    }

    public Column(String name) {
        this.entries = new ArrayList<>();
        this.name = name;
    }

    public void addEntry(Object value) {
        entries.add(new Value(value));
    }

    public String getName() {
        return name;
    }

    public List<Value> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
