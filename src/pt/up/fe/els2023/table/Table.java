package pt.up.fe.els2023.table;

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

        return true;
    }

    @Override
    public void addRow(List<Object> values) {
        rows.add(new Row(values));

        for (int i = 0; i < columns.size(); i++) {
            var value = values.get(i);
            columns.get(i).addEntry(value);
        }
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
}