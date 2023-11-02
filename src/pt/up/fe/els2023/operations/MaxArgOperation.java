package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;

public class MaxArgOperation implements TableOperation {

    private final String columnName;

    public MaxArgOperation(String columnName) {
        this.columnName = columnName;
    }

    public void accept(BTCinterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public BeginTableCascade execute(BeginTableCascade btc) throws ColumnNotFoundException {
        var maxValue = btc.max(columnName);

        if (maxValue.isEmpty()) {
            var table = new Table();
            var firstRow = btc.get().getRows().get(0);
            table.addRow(firstRow.getValues());
            return new BeginTableCascade(table);
        } else {
            return btc.where(rowWrapper -> rowWrapper.get(columnName).equals(maxValue.get()));
        }
    }
}
