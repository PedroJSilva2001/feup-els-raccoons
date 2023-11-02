package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Value;

import java.util.Map;

public class BTCinterpreter {
    private BeginTableCascade btc;
    private final Map<String, ITable> tables;

    public BTCinterpreter(BeginTableCascade btc, Map<String, ITable> tables) {
        this.btc = btc;
        this.tables = tables;
    }

    public void apply(MaxArgOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(MinArgOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(ConcatHorizontalOperation operation) {
        var additionalTableNames = operation.additionalTableNames();
        var additionalTables = new ITable[additionalTableNames.size()];
        for (int i = 0; i < additionalTableNames.size(); i++) {
            additionalTables[i] = tables.get(additionalTableNames.get(i));
        }
        btc = operation.execute(btc, additionalTables);
    }

    public BeginTableCascade getBtc() {
        return btc;
    }
}
