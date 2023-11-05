package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;

public class ArgMinOperation implements TableOperation {

    private final String columnName;

    public ArgMinOperation(String columnName) {
        this.columnName = columnName;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) throws ColumnNotFoundException {
        btcInterpreter.apply(this);
    }

    public TableCascade execute(TableCascade btc) throws ColumnNotFoundException {
        var minValue = btc.min(columnName);

        if (minValue.isEmpty()) {
            var table = new Table();
            var firstRow = btc.get().getRows().get(0);
            table.addRow(firstRow.getValues());
            return new TableCascade(table);
        } else {
            return btc.where(rowWrapper -> rowWrapper.get(columnName).equals(minValue.get()));
        }
    }
}
