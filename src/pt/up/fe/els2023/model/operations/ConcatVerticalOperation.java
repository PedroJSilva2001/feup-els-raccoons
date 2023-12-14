package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ConcatVerticalOperation extends TableOperation {
    private final List<Table> otherTables;

    public ConcatVerticalOperation(List<Table> otherTables) {
        this.otherTables = otherTables;
    }

    @Override
    public String getName() {
        var tableNames = new ArrayList<String>();

        for (var table : otherTables) {
            tableNames.add(table.getName());
        }

        return "ConcatVertical( " + String.join(", ", tableNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        // stacked on top of each other

        Table newTable = new RacoonTable(table.getColumNames());

        for (var other : otherTables) {
            // ignores repeated columns
            for (var column : other.getColumns()) {
                var name = column.getName();
                newTable.addColumn(name);
            }
        }

        var tableRowIt = table.rowIterator();

        while (tableRowIt.hasNext()) {
            var row = new ArrayList<>(tableRowIt.next().getValues());

            // the nulls are always added to the end of the row because the first table will produce
            // the first n unique columns, where n is the number of columns of the first table
            List<Value> nulls = Collections.nCopies(newTable.getColumnNumber() - table.getColumnNumber(), Value.ofNull());

            row.addAll(nulls);

            newTable.addRow(row);
        }

        for (var other : otherTables) {
            var otherRowIt = other.rowIterator();

            var otherColumnNameIndexMapping = new HashMap<String, Integer>();

            for (int i = 0; i < other.getColumnNumber(); i++) {
                otherColumnNameIndexMapping.put(other.getColumn(i).getName(), i);
            }

            while (otherRowIt.hasNext()) {
                var row = new ArrayList<Value>();

                var otherRow = otherRowIt.next();

                for (var newTableColumn : newTable.getColumns()) {
                    var name = newTableColumn.getName();

                    if (otherColumnNameIndexMapping.containsKey(name)) {
                        row.add(otherRow.get(otherColumnNameIndexMapping.get(name)));
                    } else {
                        row.add(Value.ofNull());
                    }
                }

                newTable.addRow(row);
            }
        }

        return OperationResult.ofTable(newTable);
    }

}
