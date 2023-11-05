package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

import java.util.List;

public class RejectOperation implements TableOperation {

    private final List<String> columnsList;

    public RejectOperation(List<String> columnsList) {
        this.columnsList = columnsList;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public TableCascade execute(TableCascade btc) throws ColumnNotFoundException {
        return btc.reject(columnsList.toArray(String[]::new));
    }
}
