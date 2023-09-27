package pt.up.fe.els2023.table;

import pt.up.fe.els2023.sources.TableSource;

import java.util.List;

public interface ITable {
    String getName();
    List<Column> getColumns();
    List<Row> getRows();
    TableSource getSource();
}
