package pt.up.fe.els2023;

import java.util.List;

public interface ITable {
    String getName();
    List<Column> getColumns();
    List<Row> getRows();
    TableSource getSource();
}
