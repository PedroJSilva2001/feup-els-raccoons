package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;

public class MinArgOperation implements TableOperation {

    private final String columnName;

    public MinArgOperation(String columnName) {
        this.columnName = columnName;
    }

    public void accept(BTCinterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public BeginTableCascade execute(BeginTableCascade btc) throws ColumnNotFoundException {
        var minValue = btc.min(columnName);

        if (minValue.isEmpty()) {
            var table = new Table();
            var firstRow = btc.get().getRows().get(0);
            table.addRow(firstRow.getValues());
            return new BeginTableCascade(table);
        } else {
            return btc.where(rowWrapper -> rowWrapper.get(columnName).equals(minValue.get()));
        }
    }
}
