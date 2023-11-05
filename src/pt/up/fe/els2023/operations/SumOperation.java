package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Value;

import java.util.Optional;

public class SumOperation implements TableOperation {

    private final String columnName;

    public SumOperation(String columnName) {
        this.columnName = columnName;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public Optional<Value> execute(TableCascade btc) throws ColumnNotFoundException {
        return btc.sum(columnName);
    }
}
