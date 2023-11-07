package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameOperation extends TableOperation {

    private final List<String> columnNames;
    private final List<String> newColumnNames;

    public RenameOperation(String initialTable, String resultVariableName, List<String> columnNames, List<String> newColumnNames) {
        super(initialTable, resultVariableName);
        this.columnNames = columnNames;
        this.newColumnNames = newColumnNames;
    }

    @Override
    public String getName() {
        return "Rename( " + String.join(", ", columnNames) + " ->" + String.join(", ", newColumnNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        Map<String, String> columnsMapping = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            columnsMapping.put(columnNames.get(i), newColumnNames.get(i));
        }
        return new OperationResult(tableCascade.rename(columnsMapping));
    }
}
