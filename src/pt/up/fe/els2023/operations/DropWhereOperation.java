package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.util.Map;

public class DropWhereOperation extends WhereOperation {

    public DropWhereOperation(String predicate) {
        super(predicate);
    }

    @Override
    public String name() {
        return "DropWhere( " + predicate + " )";
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var pred = parsePredicate(predicate, variablesTable.getVariables());

        return new OperationResult(previousResult.getTableCascade().dropWhere(pred));
    }
}