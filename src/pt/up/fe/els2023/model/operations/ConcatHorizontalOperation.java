package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.*;

public class ConcatHorizontalOperation extends TableOperation {

    private final List<Table> otherTables;

    public ConcatHorizontalOperation(List<Table> otherTables) {
        this.otherTables = otherTables;
    }

    @Override
    public String getName() {
        var tableNames = new ArrayList<String>();

        for (var table : otherTables) {
            tableNames.add(table.getName());
        }

        return "ConcatHorizontal( " + String.join(", ", tableNames) + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    // TODO: optimize O(C*R) -> O(C + R)
    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        // side by side

        Table newTable = new RacoonTable();

        Map<String, Integer> columnNameSuffixCount = new HashMap<>();

        var othersList = new ArrayList<>(otherTables);
        othersList.add(0, table);

        for (var other : othersList) {
            var it1 = newTable.rowIterator();
            var it2 = other.rowIterator();
            var rows = new ArrayList<List<Value>>();

            while (it1.hasNext() && it2.hasNext()) {
                var row = new ArrayList<Value>();

                var row1 = it1.next();
                var row2 = it2.next();

                row.addAll(row1.getValues());
                row.addAll(row2.getValues());

                rows.add(row);
            }

            while (it1.hasNext()) {
                var row = new ArrayList<Value>();

                var row1 = it1.next();

                List<Value> nulls = Collections.nCopies(other.getColumnNumber(), Value.ofNull());

                row.addAll(row1.getValues());
                row.addAll(nulls);

                rows.add(row);
            }

            while (it2.hasNext()) {
                var row = new ArrayList<Value>();

                var row2 = it2.next();

                List<Value> nulls = Collections.nCopies(newTable.getColumnNumber(), Value.ofNull());

                row.addAll(nulls);
                row.addAll(row2.getValues());

                rows.add(row);
            }

            for (var column : other.getColumns()) {
                var name = column.getName();
                if(columnNameSuffixCount.containsKey(name)){
                    int count = columnNameSuffixCount.get(name);
                    var suffixedName = name + "_" + (count + 1);

                    while(columnNameSuffixCount.containsKey(suffixedName)){
                        count++;
                        suffixedName = name + "_" + (count + 1);
                    }

                    newTable.addColumn(suffixedName);
                    columnNameSuffixCount.put(suffixedName, 0);
                    columnNameSuffixCount.put(name,count);
                } else{
                    newTable.addColumn(name);
                    columnNameSuffixCount.put(name, 0);
                }
            }
            newTable = new RacoonTable(newTable.getColumNames());
            for (var row : rows) {
                newTable.addRow(row);
            }
        }

        return OperationResult.ofTable(newTable);
    }
}
