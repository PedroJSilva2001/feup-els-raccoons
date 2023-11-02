package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

import java.util.List;

public class RejectOperation implements TableOperation {

    private final List<String> columnsList;

    public RejectOperation(List<String> columnsList) {
        this.columnsList = columnsList;
    }

    public void accept(BTCinterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public BeginTableCascade execute(BeginTableCascade btc) throws ColumnNotFoundException {
        return btc.reject(columnsList.toArray(String[]::new));
    }
}
