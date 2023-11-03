package pt.up.fe.els2023.table;

import java.util.List;

public interface ITable {
    String getName();

    List<Column> getColumns();

    List<Row> getRows();

    boolean addColumn(String columnName);

    boolean addRow(List<Value> values);

    int getColumnNumber();

    int getRowNumber();

    Column getColumn(int index);

    Column getColumn(String name);

    Row getRow(int index);

}
