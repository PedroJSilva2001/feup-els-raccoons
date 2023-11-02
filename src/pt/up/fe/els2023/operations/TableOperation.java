package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

public interface TableOperation {
    void accept(BTCinterpreter btcInterpreter) throws ColumnNotFoundException;
}
