package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Row {
    private final List<Value> values;

    public Row(List<Value> values) {
        this.values = new ArrayList<>();
        this.values.addAll(values);
    }

    public Row() {
        this.values = new ArrayList<>();
    }

    public void addValue(Value value) {
        this.values.add(value);
    }

    public List<Object> getValues() {
        return Collections.unmodifiableList(values);
    }

    public Value get(int index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }

        return values.get(index);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Row other)) {
            return false;
        }

        if (this.values.size() != other.values.size()) {
            return false;
        }

        for (int i = 0; i < this.values.size(); i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }

        return true;
    }
}
