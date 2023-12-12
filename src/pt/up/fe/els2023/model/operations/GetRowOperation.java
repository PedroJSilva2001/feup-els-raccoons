package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonGroupTable;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

import java.util.List;

public class GetRowOperation extends TableOperation {
    private final int rowNumber;

    public GetRowOperation(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public String getName() {
        return "GetRow(" + rowNumber + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(RacoonGroupTable table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        var result = super.execute(table);

        if (result.getType() == OperationResult.Type.GROUP_TABLE) {
            return OperationResult.ofTable(result.getGroupTable().asTable());
        } else {
            return result;
        }
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        var newTable = new RacoonTable(table.getColumNames());

        if (rowNumber < 0 || rowNumber >= table.getRowNumber()) {
            return OperationResult.ofTable(newTable);
        }

        newTable.addRow(table.getRow(rowNumber).getValues());

        return OperationResult.ofTable(newTable);
    }
}