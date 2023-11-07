package pt.up.fe.els2023.interpreter.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.TableCascade;

import java.io.IOException;

public abstract class TableOperation {

    protected String initialTable;

    protected String resultVariableName;

    public TableOperation(String initialTable, String resultVariableName) {
        this.initialTable = initialTable;
        this.resultVariableName = resultVariableName;
    }

    public String getInitialTable() {
        return initialTable;
    }

    public String getResultVariableName() {
        return resultVariableName;
    }

    public abstract String getName();

    public abstract boolean isTerminal();

    public abstract OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException;
}
