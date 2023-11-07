package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;
import java.util.List;

public class CompositeOperation extends TableOperation {

    private final List<TableOperation> operations;

    public CompositeOperation(String initialTable, String resultVariableName, List<TableOperation> operations) {
        super(initialTable, resultVariableName);
        this.operations = operations;
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder("CompositeOperation[ " + initialTable + " -> ");

        for (var operation : operations) {
            name.append(operation.getName()).append(" -> ");
        }

        name.append(resultVariableName).append(" ]");

        return name.toString();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        OperationResult result = null;

        if (operations.isEmpty()) {
            return result;
        }

        result = new OperationResult(tableCascade);

        for (int i = 0; i < operations.size(); i++) {

            var operation = operations.get(i);

            if (i != operations.size() - 1 && !operation.isTerminal()) {
                throw new ImproperTerminalOperationException(operation.getName());
            }

            result = operation.execute(result.getTableCascade(), variablesTable);
        }

        return result;
    }

}
