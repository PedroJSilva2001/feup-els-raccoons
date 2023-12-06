package pt.up.fe.els2023.table;


import pt.up.fe.els2023.dsl.TableCascade;

import java.util.Iterator;
import java.util.List;

public interface Table {
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

    TableCascade btc();

    boolean containsColumn(String name);

    int getIndexOfColumn(String name);

    Iterator<Row> rowIterator();

    List<String> getColumNames();
}
