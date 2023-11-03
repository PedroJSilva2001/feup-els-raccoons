package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.Map;

public class BTCinterpreter {
    private BeginTableCascade btc;
    private final Map<String, ITable> tables;

    public BTCinterpreter(BeginTableCascade btc, Map<String, ITable> tables) {
        this.btc = btc;
        this.tables = tables;
    }

    public void apply(ArgMaxOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(ArgMinOperation operation) throws ColumnNotFoundException {
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

    public void apply(SelectOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(RejectOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(ExportOperation operation) throws TableNotFoundException, IOException {
        operation.execute(btc);
    }

    public void apply(WhereOperation operation) {
        btc = operation.execute(btc);
    }

    public BeginTableCascade getBtc() {
        return btc;
    }
}
