package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.util.Optional;

public class MaxOperation implements TableOperation {

    private final String columnName;

    public MaxOperation(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String name() {
        return "Max( " + columnName + " )";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var result = previousResult.getTableCascade().max(columnName);

        return new OperationResult(result.orElse(null));
    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return null;
    }
}
