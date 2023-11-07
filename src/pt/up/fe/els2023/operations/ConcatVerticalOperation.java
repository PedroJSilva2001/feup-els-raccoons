package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConcatVerticalOperation extends TableOperation {
    private final List<String> additionalTableNames;

    public ConcatVerticalOperation(String initialTable, String resultVariableName, List<String> additionalTableNames) {
        super(initialTable, resultVariableName);
        this.additionalTableNames = additionalTableNames;
    }

    @Override
    public String getName() {
        return "ConcatVertical( " + String.join(", ", additionalTableNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        var tables = new ArrayList<Table>();

        for (String tableName : additionalTableNames) {
            if (!variablesTable.hasTable(tableName)) {
                throw new TableNotFoundException(tableName);
            }

            tables.add(variablesTable.getTable(tableName));
        }

        return new OperationResult(tableCascade.concatVertical(tables.toArray(Table[]::new)));

    }

}
