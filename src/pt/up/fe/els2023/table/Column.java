package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private final String name;

    private final List<Object> entries;

    public Column(String name, List<Object> entries) {
        this.entries = entries;
        this.name = name;
    }

    public Column(String name) {
        this.entries = new ArrayList<>();
        this.name = name;
    }

    public void addEntry(Object value) {
        entries.add(value);
    }

    public String getName() {
        return name;
    }

    public List<Object> getEntries() {
        return entries;
    }
}
