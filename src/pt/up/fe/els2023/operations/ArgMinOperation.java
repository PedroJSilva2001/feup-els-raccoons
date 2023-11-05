package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

public class ArgMinOperation implements TableOperation {

    private final String columnName;

    public ArgMinOperation(String columnName) {
        this.columnName = columnName;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public TableCascade execute(TableCascade btc) throws ColumnNotFoundException {
        return btc.argMin(columnName);
    }
}
