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
}