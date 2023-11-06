package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;

import java.io.IOException;
import java.util.List;

public record CompositeOperation(
        String initialTable,
        String resultVariableName,
        List<TableOperation> operations
        ) implements TableOperation {

    @Override
    public String name() {
        StringBuilder name = new StringBuilder("CompositeOperation[ " + initialTable + " -> ");

        for (var operation : operations) {
            name.append(operation.name()).append(" -> ");
        }

        name.append(resultVariableName).append(" ]");

        return name.toString();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        // not necessary for a composite operation but can be implemented
        return null;
    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        OperationResult result = null;

        if (operations.isEmpty()) {
            return result;
        }

        if (!variablesTable.hasTable(initialTable)) {
            throw new TableNotFoundException(initialTable);
        }


        var table = variablesTable.getTable(initialTable);

        result = new OperationResult(new TableCascade(table));

        for (int i = 0; i < operations.size(); i++) {

            var operation = operations.get(i);

            if (i != operations.size() - 1 && !operation.isTerminal()) {
                throw new ImproperTerminalOperationException(operation.name());
            }

            result = operation.execute(result, variablesTable);
        }

        return result;
    }
}
