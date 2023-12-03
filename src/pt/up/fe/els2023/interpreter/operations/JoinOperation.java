package pt.up.fe.els2023.interpreter.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.TableCascade;

import java.io.IOException;

public class JoinOperation extends TableOperation {

    private final String onColumn;
    private final String otherTable;

    public JoinOperation(String initialTable, String resultVariableName, String otherTable, String onColumn) {
        super(initialTable, resultVariableName);
        this.otherTable = otherTable;
        this.onColumn = onColumn;
    }

    @Override
    public String getName() {
        return "Join( " + otherTable + ", " + onColumn + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var otherTable = variablesTable.getTable(this.otherTable);

        if (otherTable == null) {
            throw new TableNotFoundException(this.otherTable);
        }

        return new OperationResult(tableCascade.join(otherTable, onColumn));
    }
}
