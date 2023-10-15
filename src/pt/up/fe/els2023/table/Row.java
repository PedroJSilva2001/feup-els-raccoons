package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Row {
    private final List<Value> values;

    public Row(List<Object> values) {
        this.values = new ArrayList<>();

        for (Object value : values) {
            this.values.add(new Value(value));
        }
    }

    public Row() {
        this.values = new ArrayList<>();
    }

    public Row(Row row) {
        this.values = new ArrayList<>();

        for (Value value : row.values) {
            values.add(new Value(value.value()));
        }
    }

    public void addValue(Object value) {
        this.values.add(new Value(value));
    }

    public List<Object> getValues() {
        return Collections.unmodifiableList(values);
    }
}
