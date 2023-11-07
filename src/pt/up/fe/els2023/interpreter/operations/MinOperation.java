package pt.up.fe.els2023.interpreter.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.TableCascade;

import java.io.IOException;

public class MinOperation extends TableOperation {

    private final String columnName;

    public MinOperation(String initialTable, String resultVariableName, String columnName) {
        super(initialTable, resultVariableName);
        this.columnName = columnName;
    }

    @Override
    public String getName() {
        return "Min( " + columnName + " )";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var result = tableCascade.min(columnName);

        return new OperationResult(result.orElse(null));
    }
}
