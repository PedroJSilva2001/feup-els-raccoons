package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

public class CountOperation implements TableOperation {

    private final String columnName;

    public CountOperation(String columnName) {
        this.columnName = columnName;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public long execute(TableCascade btc) throws ColumnNotFoundException {
        return btc.count(columnName);
    }
}
