package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Pipeline {
    private final String initialTable;
    private final String result;
    private final List<TableOperation> operations;
    private final String resultVariable;

    public Pipeline(String initialTable, String result, List<TableOperation> operations) {
        this.initialTable = initialTable;
        this.result = result;
        this.operations = operations;
        this.resultVariable = null;
    }

    public Pipeline(String initialTable, List<TableOperation> operations, String resultVariable) {
        this.initialTable = initialTable;
        this.result = null;
        this.operations = operations;
        this.resultVariable = resultVariable;
    }

    public Pipeline(String initialTable, String result, List<TableOperation> operations, String resultVariable) {
        this.initialTable = initialTable;
        this.result = result;
        this.operations = operations;
        this.resultVariable = resultVariable;
    }

    public TableCascadeInterpreter updateBTC(Map<String, ITable> tables, Map<String, Value> resultVariables) {
        var btc = new TableCascade(tables.get(initialTable));
        var btcInterpreter = new TableCascadeInterpreter(btc, tables, resultVariables);
        for (var operation : operations) {
            try {
                operation.accept(btcInterpreter);
            } catch (TableNotFoundException | IOException | ColumnNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return btcInterpreter;
    }

    public String getResult() {
        return result;
    }

    public String getResultVariable() {
        return resultVariable;
    }
}
