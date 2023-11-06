package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameOperation implements TableOperation {

    private final List<String> columnNames;
    private final List<String> newColumnNames;

    public RenameOperation(List<String> columnNames, List<String> newColumnNames) {
        this.columnNames = columnNames;
        this.newColumnNames = newColumnNames;
    }

    @Override
    public String name() {
        return "Rename( " + String.join(", ", columnNames) + " ->" + String.join(", ", newColumnNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        Map<String, String> columnsMapping = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            columnsMapping.put(columnNames.get(i), newColumnNames.get(i));
        }
        return new OperationResult(previousResult.getTableCascade().rename(columnsMapping));
    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return null;
    }
}
