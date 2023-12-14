package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

public class LimitOperation extends TableOperation {
    public final int count;

    public LimitOperation(int count) {
        this.count = count;
    }

    @Override
    public String getName() {
        return "Limit(" + count + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        var newTable = new RacoonTable(table.getColumNames());

        for(int row = 0; row < count && row < table.getRowNumber(); row++) {
            newTable.addRow(table.getRow(row).getValues());
        }

        return OperationResult.ofTable(newTable);
    }
}
