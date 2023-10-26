package pt.up.fe.els2023.table;

import pt.up.fe.els2023.sources.TableSource;

import java.util.List;

public interface ITable {
    String getName();

    List<Column> getColumns();

    List<Row> getRows();

    TableSource getSource();

    boolean addColumn(String columnName);

    boolean addRow(List<Value> values);

    int getColumnNumber();

    int getRowNumber();

    Column getColumn(int index);

    Column getColumn(String name);

    Row getRow(int index);

}
