package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;

public class ArgMinOperation extends TableOperation {

    private final String columnName;

    public ArgMinOperation(String initialTable, String resultVariableName, String columnName) {
        super(initialTable, resultVariableName);
        this.columnName = columnName;
    }

    @Override
    public String getName() {
        return "ArgMin( " + columnName + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return new OperationResult(tableCascade.argMin(columnName));
    }

}
