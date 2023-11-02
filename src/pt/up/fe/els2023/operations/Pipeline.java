package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Pipeline {
    private final String initialTable;
    private final String result;
    private final List<TableOperation> operations;

    public Pipeline(String initialTable, String result, List<TableOperation> operations) {
        this.initialTable = initialTable;
        this.result = result;
        this.operations = operations;
    }

    public BTCinterpreter updateBTC(Map<String, ITable> tables) {
        var btc = new BeginTableCascade(tables.get(initialTable));
        var btcInterpreter = new BTCinterpreter(btc, tables);
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
}
