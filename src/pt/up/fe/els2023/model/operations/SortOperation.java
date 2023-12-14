package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;

public class SortOperation extends TableOperation {
    private final String columnName;
    private final boolean ascending;

    public SortOperation(String columnName, boolean ascending) {
        this.columnName = columnName;
        this.ascending = ascending;
    }

    @Override
    public String getName() {
        return "Sort(" + columnName + " " + (ascending? "ASC" : "DESC") + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {

        var commonRep = table.getColumn(columnName).getMostGeneralRep();
        var columnIndex = table.getIndexOfColumn(columnName);

        if(commonRep == null) {
            return OperationResult.ofTable(new RacoonTable(table.getColumNames()));
        }

        var rowsCopy = new ArrayList<>(table.getRows());

        rowsCopy.sort((r1, r2) -> {
            var v1 = r1.get(columnIndex);
            var v2 = r2.get(columnIndex);

            if(v1.isNull() && v2.isNull()){
                return 0;
            }

            if(v1.isNull()){
                return 1;
            }

            if(v2.isNull()){
                return -1;
            }

            var castedV1 = commonRep.cast(v1);
            var castedV2 = commonRep.cast(v2);

            return ascending ? commonRep.comparator().compare(castedV1, castedV2) : commonRep.comparator().compare(castedV2, castedV1);
        });

        var newTable = new RacoonTable(table.getColumNames());

        for (var row : rowsCopy) {
            newTable.addRow(row.getValues());
        }

        return OperationResult.ofTable(newTable);
    }
}