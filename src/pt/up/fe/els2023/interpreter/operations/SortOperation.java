package pt.up.fe.els2023.interpreter.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.TableCascade;

import java.io.IOException;

public class SortOperation extends TableOperation {
    private final String columnName;
    private final boolean ascending;

    public SortOperation(String initialTable, String resultVariableName, String columnName, boolean ascending) {
        super(initialTable, resultVariableName);
        this.columnName = columnName;
        this.ascending = ascending;
    }

    @Override
    public String getName() {
        return "Sort(" + columnName + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return new OperationResult(tableCascade.sort(ascending, columnName));
    }
}