package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record ConcatVerticalOperation(List<String> additionalTableNames) implements TableOperation {

    @Override
    public String name() {
        return "ConcatVertical( " + String.join(", ", additionalTableNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var tables = new ArrayList<ITable>();

        for (String tableName : additionalTableNames) {
            if (!variablesTable.hasTable(tableName)) {
                throw new TableNotFoundException(tableName);
            }

            tables.add(variablesTable.getTable(tableName));
        }

        return new OperationResult(previousResult.getTableCascade().concatVertical(tables.toArray(ITable[]::new)));

    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return null;
    }
}
