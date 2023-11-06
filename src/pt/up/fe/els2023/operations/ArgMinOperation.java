package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;

public class ArgMinOperation implements TableOperation {

    private final String columnName;

    public ArgMinOperation(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String name() {
        return "ArgMin( " + columnName + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return new OperationResult(previousResult.getTableCascade().argMin(columnName));
    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return null;
    }
}
