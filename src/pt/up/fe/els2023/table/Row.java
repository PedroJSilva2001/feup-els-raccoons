package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private final List<Object> values;

    public Row(List<Object> values) {
        this.values = values;
    }

    public Row() {
        this.values = new ArrayList<>();
    }

    public void addValue(Object value) {
        this.values.add(value);
    }
}
