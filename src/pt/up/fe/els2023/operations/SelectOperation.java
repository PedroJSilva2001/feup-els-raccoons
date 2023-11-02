package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

import java.util.List;

public class SelectOperation implements TableOperation {

    private final List<String> columnsList;

    public SelectOperation(List<String> columnsList) {
        this.columnsList = columnsList;
    }

    public void accept(BTCinterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public BeginTableCascade execute(BeginTableCascade btc) throws ColumnNotFoundException {
        return btc.select(columnsList.toArray(String[]::new));
    }
}
