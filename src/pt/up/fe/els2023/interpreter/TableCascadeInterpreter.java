package pt.up.fe.els2023.interpreter;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.dsl.TableCascade;
import pt.up.fe.els2023.model.operations.TableOperation;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;

public class TableCascadeInterpreter {
    private TableCascade btc;

    private Value valueResult;

    private final VariablesTable variablesTable;

    public TableCascadeInterpreter(VariablesTable variablesTable) {
        this.variablesTable = variablesTable;
        this.btc = null;
        this.valueResult = null;
    }

    public void execute(TableOperation operation) throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        /*var initialTable = operation.getInitialTable();

        if (!variablesTable.hasTable(initialTable)) {
            throw new TableNotFoundException(initialTable);
        }

         var table = variablesTable.getTable(initialTable);

        var result = operation.execute(table.btc(), variablesTable);

        if (result == null) {
            return;
        }

        if (result.hasValue()) {
            variablesTable.putValue(operation.getResultVariableName(), result.getValue());
        } else {
            variablesTable.putTable(operation.getResultVariableName(), result.getTable());
        }*/
    }
}
