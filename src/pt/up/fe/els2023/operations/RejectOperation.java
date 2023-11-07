package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RejectOperation extends TableOperation {

    private final List<String> columnsList;

    public RejectOperation(String initialTable, String resultVariableName, List<String> columnsList) {
        super(initialTable, resultVariableName);
        this.columnsList = columnsList;
    }

    @Override
    public String getName() {
        return "Reject( " + String.join(", ", columnsList) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return new OperationResult(tableCascade.reject(columnsList.toArray(String[]::new)));
    }
}
