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

    // TODO cache the number representation; update it when new entries are added
    public Value.Type getMostGeneralNumberRep() {
        if (entries.isEmpty()) {
            return null;
        }

        Value.Type currType = null;

        for (var entry : entries) {
            if (!entry.isNumber()) {
                continue;
            }

            if (currType == null) {
                currType = entry.getType();
                continue;
            }

            assert currType != Value.Type.NULL && entry.getType() != Value.Type.NULL &&
                    currType != Value.Type.BOOLEAN && entry.getType() != Value.Type.BOOLEAN &&
                    currType != Value.Type.STRING && entry.getType() != Value.Type.STRING;

            if (currType == entry.getType()) {
                continue;
            }

            if ((currType == Value.Type.BIG_INTEGER && entry.getType() == Value.Type.LONG) ||
                    (currType == Value.Type.LONG && entry.getType() == Value.Type.BIG_INTEGER)) {
                currType = Value.Type.BIG_INTEGER;

            } else if ((currType == Value.Type.BIG_DECIMAL && entry.getType() == Value.Type.DOUBLE) ||
                    (currType == Value.Type.DOUBLE && entry.getType() == Value.Type.BIG_DECIMAL)) {
                currType = Value.Type.BIG_DECIMAL;

            } else {
                // the following conversions:
                //      big integer <-> big decimal
                //      big integer <-> double
                //      long <-> big decimal
                //      long <-> double
                // default to big decimal
                currType = Value.Type.BIG_DECIMAL;
            }

        }

        return currType;
    }
}
