package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;

import java.io.IOException;

public interface TableOperation {
    void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException, TableNotFoundException, IOException;
}
