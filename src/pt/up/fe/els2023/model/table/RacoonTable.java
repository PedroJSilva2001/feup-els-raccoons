package pt.up.fe.els2023.model.table;

import pt.up.fe.els2023.dsl.TableCascade;

import java.util.*;

public class RacoonTable implements Table {
    private final String name;
    private final List<Column> columns;
    private final List<Row> rows;

    public RacoonTable() {
        this.name = null;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public RacoonTable(boolean withFileColumn) {
        this.name = null;
        this.columns = new ArrayList<>();
        if (withFileColumn) {
            this.columns.add(new Column("File"));
        }
        this.rows = new ArrayList<>();
    }

    public RacoonTable(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }


    // Empty table, just with same column names
    public RacoonTable(List<String> columnNames) {
        this.columns = new ArrayList<>();

        for (var columnName : columnNames) {
            this.columns.add(new Column(columnName));
        }

        this.rows = new ArrayList<>();

        this.name = null;
    }

    // Deep copy
    public RacoonTable(Table table) {
        this.name = table.getName();
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();

        for (var column : table.getColumns()) {
            this.columns.add(new Column(column.getName()));
        }

        for (var row : table.getRows()) {
            addRow(row.getValues());
        }
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
    public boolean addColumn(String columnName) {
        for (var column : columns) {
            if (column.getName().equals(columnName)) {
                return false;
            }
        }

        columns.add(new Column(columnName));

        for (var row : rows) {
            row.addValue(Value.ofNull());
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


    // btc = begin table cascade
    @Override
    public TableCascade btc() {
        return new TableCascade(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof RacoonTable other)) {
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

    @Override
    public int getIndexOfColumn(String name) {
        for (int i = 0; i < getColumnNumber(); i++) {
            if (getColumn(i).getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean containsColumn(String name) {
        return getIndexOfColumn(name) != -1;
    }

    @Override
    public Iterator<Row> rowIterator() {
        return new RowIterator();
    }

    private class RowIterator implements Iterator<Row> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < getRowNumber();
        }

        @Override
        public Row next() {
            if (!hasNext()) {
                return null;
            }

            return getRow(currentIndex++);
        }
    }

    @Override
    public List<String> getColumNames() {
        List<String> columnNames = new ArrayList<>();

        for (var column : columns) {
            columnNames.add(column.getName());
        }

        return columnNames;
    }
}