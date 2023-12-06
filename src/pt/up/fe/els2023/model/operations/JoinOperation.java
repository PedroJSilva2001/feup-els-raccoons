package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;

import pt.up.fe.els2023.imports.ColumnUtils;
import pt.up.fe.els2023.table.RacoonTable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Column;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class JoinOperation extends TableOperation {

    private final String onColumn;
    private final Table otherTable;

    public JoinOperation(Table otherTable, String onColumn) {
        this.onColumn = onColumn;
        this.otherTable = otherTable;
    }

    @Override
    public String getName() {
        return "Join( " + otherTable + ", " + onColumn + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }


    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        var col1 = table.getIndexOfColumn(onColumn);
        var col2 = otherTable.getIndexOfColumn(onColumn);

        if (col1 == -1 || col2 == -1) {
            throw new ColumnNotFoundException(onColumn);
        }

        var newTable = new RacoonTable(table.getColumNames());
        var columnNames = newTable.getColumns().stream().map(Column::getName).collect(Collectors.toCollection(HashSet::new));

        for (var otherColumn : otherTable.getColumns()) {
            var name = otherColumn.getName();

            if (!name.equals(onColumn)) {
                var uniqueName = ColumnUtils.makeUnique(name, columnNames);

                newTable.addColumn(uniqueName);
                columnNames.add(uniqueName);
            }
        }

        for (var row1 : table.getRows()) {
            for (var row2 : otherTable.getRows()) {
                if (row1.get(col1).equals(row2.get(col2))) {
                    var newRow = new ArrayList<>(row1.getValues());

                    for (int i = 0; i < row2.getValues().size(); i++) {
                        if (i != col2) {
                            newRow.add(row2.get(i));
                        }
                    }

                    newTable.addRow(newRow);
                }
            }
        }

        return OperationResult.ofTable(newTable);

    }
}
