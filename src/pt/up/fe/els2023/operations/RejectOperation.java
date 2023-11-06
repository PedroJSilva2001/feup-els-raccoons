package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RejectOperation implements TableOperation {

    private final List<String> columnsList;

    public RejectOperation(List<String> columnsList) {
        this.columnsList = columnsList;
    }

    @Override
    public String name() {
        return "Reject( " + String.join(", ", columnsList) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return new OperationResult(previousResult.getTableCascade().reject(columnsList.toArray(String[]::new)));
    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return null;
    }
}
