package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;

import java.util.List;

public record ConcatHorizontalOperation(List<String> additionalTableNames) implements TableOperation {

    public void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public TableCascade execute(TableCascade initialBTC, ITable... additionalTables) {
        return initialBTC.concatHorizontal(additionalTables);
    }
}
