package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeginTableCascade {
    private final ITable table;

    public BeginTableCascade(ITable table) {
        this.table = table;
    }

    public ITable get() {
        return table;
    }

    public BeginTableCascade select(String ...columns) throws ColumnNotFoundException {
        ITable newTable = new Table(false);

        var columnsToKeep = new TreeSet<Integer>();

        for (var column : columns) {
            if (!table.containsColumn(column)) {
                throw new ColumnNotFoundException(column);
            }

            newTable.addColumn(column);

            columnsToKeep.add(table.getIndexOfColumn(column));
        }

        if (columnsToKeep.isEmpty()) {
            return new BeginTableCascade(newTable);
        }

        for (var row : table.getRows()) {
            List<Value> values = new ArrayList<>();

            for (var column : columnsToKeep) {
                values.add(row.get(column));
            }

            newTable.addRow(values);
        }

        return new BeginTableCascade(newTable);
    }

    public BeginTableCascade reject(String ...columns) throws ColumnNotFoundException {
        for (var column : columns) {
            if (!table.containsColumn(column)) {
                throw new ColumnNotFoundException(column);
            }
        }

        var columnsToKeep = new ArrayList<String>();

        for (var column : table.getColumns()) {
            if (!Arrays.asList(columns).contains(column.getName())) {
                columnsToKeep.add(column.getName());
            }
        }

        return select(columnsToKeep.toArray(String[]::new));
    }

    public BeginTableCascade where(Predicate<RowWrapper> predicate) {
        ITable newTable = new Table(table);

        for (var row : table.getRows()) {
            Map<String, Value> mapping = new HashMap<>();

            for (int i = 0; i < row.getValues().size(); i++) {
                mapping.put(table.getColumn(i).getName(), row.get(i));
            }

            var wrapper = new RowWrapper(mapping);

            if (predicate.test(wrapper)) {
                newTable.addRow(row.getValues());
            }
        }

        return new BeginTableCascade(newTable);
    }

    public BeginTableCascade dropWhere(Predicate<RowWrapper> predicate) {
        return where(predicate.negate());
    }

    public BeginTableCascade sort() {
        return null;
    }

    public BeginTableCascade sortDescending() {
        return null;
    }


    public BeginTableCascade concatVertical(ITable ...others) {
        // stacked on top of each other

        ITable newTable = new Table(table);

        for (var other : others) {
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

        for (var other : others) {
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

        return new BeginTableCascade(newTable);
    }

    // TODO: optimize O(C*R) -> O(C + R)
    public BeginTableCascade concatHorizontal(ITable ...others) {
        // side by side

        ITable newTable = new Table(table);

        Map<String, Integer> columnNameSuffixCount = new HashMap<>();

        for (var column : table.getColumns()) {
            columnNameSuffixCount.put(column.getName(), 0);
        }

        for (var other : others) {

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

            var it1 = table.rowIterator();
            var it2 = other.rowIterator();

            while (it1.hasNext() && it2.hasNext()) {
                var row = new ArrayList<Value>();

                var row1 = it1.next();
                var row2 = it2.next();

                row.addAll(row1.getValues());
                row.addAll(row2.getValues());

                newTable.addRow(row);
            }

            while (it1.hasNext()) {
                var row = new ArrayList<Value>();

                var row1 = it1.next();

                List<Value> nulls = Collections.nCopies(other.getColumnNumber(), Value.ofNull());

                row.addAll(row1.getValues());
                row.addAll(nulls);

                newTable.addRow(row);
            }

            while (it2.hasNext()) {
                var row = new ArrayList<Value>();

                var row2 = it2.next();

                List<Value> nulls = Collections.nCopies(table.getColumnNumber(), Value.ofNull());

                row.addAll(nulls);
                row.addAll(row2.getValues());

                newTable.addRow(row);
            }
        }

        return new BeginTableCascade(newTable);
    }

    public long count(String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }

        return col.getEntries().stream().filter(
                (value) -> !value.isNull()
        ).count();
    }

    public Optional<Value> max(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var commonNumberRep = colValues.get(0).getType();

        return colValues.stream().max(commonNumberRep.comparator());
    }

    public Optional<Value> min(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var commonNumberRep = colValues.get(0).getType();

        return colValues.stream().min(commonNumberRep.comparator());
    }

    public Optional<Value> sum(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var commonNumberRep = colValues.get(0).getType();

        return Optional.of(colValues.stream().reduce(commonNumberRep.additiveIdentity(), Value::addS));
    }

    public Optional<Value> mean(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var sumOpt = sum(column);

        if(sumOpt.isEmpty()){
            return Optional.empty();
        }

        var sum = sumOpt.get();

        return Optional.of(sum.divide(Value.of(colValues.size())));
    }

    public Optional<Value> std(String column) {
        return Optional.empty();
    }

    public Optional<Value> var(String column) {
        return Optional.empty();
    }

    private List<Value> getColumnWithCommonNumberRep(String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }

        var commonNumberRep = col.getMostGeneralNumberRep();

        if (commonNumberRep == null) {
            // Column has no numbers
            return Collections.emptyList();
        }

        // Aggregate statistics ignore values other than numbers
        return col.getEntries().stream().filter(Value::isNumber).map(commonNumberRep::cast).collect(Collectors.toList());
    }
}
