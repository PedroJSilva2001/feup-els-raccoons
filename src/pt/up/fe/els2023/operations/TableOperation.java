package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;

public interface TableOperation {

    String name();

    boolean isTerminal();

    OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException;

    OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException;
}
