package pt.up.fe.els2023.table;

import pt.up.fe.els2023.operations.TableCascade;
import pt.up.fe.els2023.sources.TableSource;

import java.util.*;

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

    public Table(boolean withFileColumn) {
        this.name = null;
        this.columns = new ArrayList<>();
        if (withFileColumn) {
            this.columns.add(new Column("File"));
        }
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


    // Empty table, just with column names
    public Table(ITable table) {
        this.name = String.valueOf(table.getName());
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();

        for (var column : table.getColumns()) {
            this.columns.add(new Column(column.getName()));
        }

        this.source = null;
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
}