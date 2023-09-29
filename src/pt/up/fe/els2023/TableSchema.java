package pt.up.fe.els2023;

import pt.up.fe.els2023.sources.TableSource;

import java.util.List;

public class TableSchema {
    String name;
    List<ColumnSchema> columns;
    TableSource source;

    public TableSchema(String name, List<ColumnSchema> columns, TableSource source) {
        this.name = name;
        this.columns = columns;
        this.source = source;
    }
}
