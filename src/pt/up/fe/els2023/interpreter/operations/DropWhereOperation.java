package pt.up.fe.els2023.interpreter.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.TableCascade;

import java.io.IOException;

public class DropWhereOperation extends WhereOperation {
    public DropWhereOperation(String initialTable, String resultVariableName, String predicate) {
        super(initialTable, resultVariableName, predicate);
    }

    @Override
    public String getName() {
        return "DropWhere( " + predicate + " )";
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var pred = parsePredicate(predicate, variablesTable.getVariables());

        return new OperationResult(tableCascade.dropWhere(pred));
    }
}