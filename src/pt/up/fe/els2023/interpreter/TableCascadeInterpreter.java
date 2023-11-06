package pt.up.fe.els2023.interpreter;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.CompositeOperation;
import pt.up.fe.els2023.operations.TableCascade;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TableCascadeInterpreter {
    private TableCascade btc;

    private Value valueResult;

    private final VariablesTable variablesTable;

    public TableCascadeInterpreter(VariablesTable variablesTable) {
        this.variablesTable = variablesTable;
        this.btc = null;
        this.valueResult = null;
    }

    public void execute(CompositeOperation compositeOperation) throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var result = compositeOperation.execute(variablesTable);

        if (result == null) {
            return;
        }

        if (result.hasValue()) {
            variablesTable.putValue(compositeOperation.resultVariableName(), result.getValue());
        } else {
            variablesTable.putTable(compositeOperation.resultVariableName(), result.getTable());
        }
    }

    public TableCascade getBtc() {
        return btc;
    }

    /*private ITable[] getAdditionalTablesByName(List<String> additionalTableNames) {
        var additionalTables = new ITable[additionalTableNames.size()];
        for (int i = 0; i < additionalTableNames.size(); i++) {
            additionalTables[i] = tables.get(additionalTableNames.get(i));
        }
        return additionalTables;
    }*/
}
