package pt.up.fe.els2023.table;

import pt.up.fe.els2023.sources.TableSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table implements ITable {
    private final String name;
    private final List<Column> columns;
    private final List<Row> rows;
    private final TableSource source;

    public Table() {
        this.name = null;
        this.columns = new ArrayList<>();
        this.columns.add(new Column("File"));
        this.rows = new ArrayList<>();
        this.source = null;
    }

    public Table(String name, TableSource source) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.columns.add(new Column("File"));
        this.rows = new ArrayList<>();
        this.source = source;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    @Override
    public List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }

    @Override
    public TableSource getSource() {
        return source;
    }

    @Override
    public boolean addColumn(String columnName) {
        for (var column : columns) {
            if (column.getName().equals(columnName)) {
                return false;
            }
        }

        columns.add(new Column(columnName));

        for (var row : rows) {
            row.addValue(new Value(null));
        }

        return true;
    }

    @Override
    public boolean addRow(List<Value> values) {
        if (values.size() != getColumnNumber()) {
            return false;
        }

        rows.add(new Row(values));

        for (int i = 0; i < getColumnNumber(); i++) {
            var value = values.get(i);
            columns.get(i).addEntry(value);
        }

        return true;
    }

    @Override
    public int getColumnNumber() {
        return columns.size();
    }

    @Override
    public int getRowNumber() {
        return rows.size();
    }

    @Override
    public Column getColumn(int index) {
        if (index < 0 || index >= columns.size()) {
            return null;
        }

        return columns.get(index);
    }

    @Override
    public Column getColumn(String name) {
        for (var column : columns) {
            if (column.getName().equals(name)) {
                return column;
            }
        }

        return null;
    }

    @Override
    public Row getRow(int index) {
        if (index < 0 || index >= rows.size()) {
            return null;
        }

        return rows.get(index);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Table other)) {
            return false;
        }

        if (this.getColumnNumber() != other.getColumnNumber() ||
                this.getRowNumber() != other.getRowNumber()) {
            return false;
        }

        for (int i = 0; i < this.getColumnNumber(); i++) {
            if (!this.getColumn(i).getName().equals(other.getColumn(i).getName())) {
                return false;
            }
        }

        for (int i = 0; i < this.getRowNumber(); i++) {
            if (!this.getRow(i).equals(other.getRow(i))) {
                return false;
            }
        }

        return true;
    }
}