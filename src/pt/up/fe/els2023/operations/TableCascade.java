package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TableCascade {
    private final ITable table;

    public TableCascade(ITable table) {
        this.table = table;
    }

    public ITable get() {
        return table;
    }

    public TableCascade select(String ...columns) throws ColumnNotFoundException {
        ITable newTable = new Table();

        var columnsToKeep = new ArrayList<Integer>();

        for (var column : columns) {
            if (!table.containsColumn(column)) {
                throw new ColumnNotFoundException(column);
            }

            newTable.addColumn(column);

            columnsToKeep.add(table.getIndexOfColumn(column));
        }

        if (columnsToKeep.isEmpty()) {
            return new TableCascade(newTable);
        }

        for (var row : table.getRows()) {
            List<Value> values = new ArrayList<>();

            for (var column : columnsToKeep) {
                values.add(row.get(column));
            }

            newTable.addRow(values);
        }

        return new TableCascade(newTable);
    }

    public TableCascade reject(String ...columns) throws ColumnNotFoundException {
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

    public TableCascade where(Predicate<RowWrapper> predicate) {
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

        return new TableCascade(newTable);
    }

    public TableCascade dropWhere(Predicate<RowWrapper> predicate) {
        return where(predicate.negate());
    }

    public TableCascade sort() {
        return null;
    }

    public TableCascade sortDescending() {
        return null;
    }


    public TableCascade concatVertical(ITable ...others) {
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

        return new TableCascade(newTable);
    }

    // TODO: optimize O(C*R) -> O(C + R)
    public TableCascade concatHorizontal(ITable ...others) {
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

        return new TableCascade(newTable);
    }

    public TableCascade rename(Map<String, String> columnMapping) {
        ITable newTable = new Table();

        for (var column : table.getColumns()) {
            var name = column.getName();
            newTable.addColumn(columnMapping.getOrDefault(name, name));
        }

        for (var row : table.getRows()) {
            newTable.addRow(row.getValues());
        }

        return new TableCascade(newTable);
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

    public Map<String, Long> count(String ...columns) throws ColumnNotFoundException {
        Map<String, Long> counts = new HashMap<>();

        for (var column : columns) {
            counts.put(column, count(column));
        }

        return counts;
    }

    public Optional<Value> max(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var commonNumberRep = colValues.get(0).getType();

        return colValues.stream().max(commonNumberRep.comparator());
    }

    public Map<String, Optional<Value>> max(String ...columns) throws ColumnNotFoundException {
        Map<String, Optional<Value>> maxes = new HashMap<>();

        for (var column : columns) {
            maxes.put(column, max(column));
        }

        return maxes;
    }

    public TableCascade argMax(String column) throws ColumnNotFoundException {
        var maxValue = max(column);

        if (maxValue.isEmpty()) {
            var table = new Table();
            table.addRow(this.table.getRows().get(0).getValues());
            return new TableCascade(table);
        } else {
            return where(rowWrapper -> rowWrapper.get(column).equals(maxValue.get()));
        }
    }

    public Optional<Value> min(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var commonNumberRep = colValues.get(0).getType();

        return colValues.stream().min(commonNumberRep.comparator());
    }

    public Map<String, Optional<Value>> min(String ...columns) throws ColumnNotFoundException {
        Map<String, Optional<Value>> mins = new HashMap<>();

        for (var column : columns) {
            mins.put(column, min(column));
        }

        return mins;
    }

    public TableCascade argMin(String column) throws ColumnNotFoundException {
        var minValue = min(column);

        if (minValue.isEmpty()) {
            var table = new Table();
            table.addRow(this.table.getRows().get(0).getValues());
            return new TableCascade(table);
        } else {
            return where(rowWrapper -> rowWrapper.get(column).equals(minValue.get()));
        }
    }

    public Optional<Value> sum(String column) throws ColumnNotFoundException {
        var colValues = getColumnWithCommonNumberRep(column);

        if (colValues.isEmpty()) {
            return Optional.empty();
        }

        var commonNumberRep = colValues.get(0).getType();

        return Optional.of(colValues.stream().reduce(commonNumberRep.additiveIdentity(), (v1, v2) -> Value.add(v1, v2)));
    }

    public Map<String, Optional<Value>> sum(String ...columns) throws ColumnNotFoundException {
        Map<String, Optional<Value>> sums = new HashMap<>();

        for (var column : columns) {
            sums.put(column, sum(column));
        }

        return sums;
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

    public Map<String, Optional<Value>> mean(String ...columns) throws ColumnNotFoundException {
        Map<String, Optional<Value>> means = new HashMap<>();

        for (var column : columns) {
            means.put(column, mean(column));
        }

        return means;
    }

    public Optional<Value> std(String column) {
        return Optional.empty();
    }

    public Map<String, Optional<Value>> std(String ...columns) throws ColumnNotFoundException {
        Map<String, Optional<Value>> stds = new HashMap<>();

        for (var column : columns) {
            stds.put(column, mean(column));
        }

        return stds;
    }

    public Optional<Value> var(String column) {
        return Optional.empty();
    }

    public Map<String, Optional<Value>> var(String ...columns) throws ColumnNotFoundException {
        Map<String, Optional<Value>> vars = new HashMap<>();

        for (var column : columns) {
            vars.put(column, mean(column));
        }

        return vars;
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
