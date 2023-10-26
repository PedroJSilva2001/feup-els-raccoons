package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.*;
import java.util.function.Predicate;

public class BeginTableCascade {
    private final ITable table;

    public BeginTableCascade(ITable table) {
        this.table = table;
    }

    public ITable get() {
        return table;
    }

    public BeginTableCascade select(String ...columns) {
        return null;
    }

    public BeginTableCascade reject(String ...columns) {
        return null;
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

    public long count(String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }

        return col.getEntries().stream().filter(
                (value) -> value.getValue() != null
        ).count();
    }

    public <T extends Number> Optional<T> max(String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }


        var anyNonNumbers = col.getEntries().stream().anyMatch(
                (value) -> !value.isNull() && !value.isLong() && !value.isDouble()
        );

        if (anyNonNumbers) {
            return Optional.empty();
        }

        var res = col.getEntries().stream().filter((value) -> value.getValue() != null).mapToDouble(
                (val) -> {
                    if (val.isLong()) {
                        return ((Long) val.getValue()).doubleValue();
                    } else {
                        return (Double) val.getValue();
                    }
                }).max();


        if (res.isEmpty()) {
            return Optional.empty();
        }

        return (Optional<T>) Optional.of(res.getAsDouble());
    }

    public <T extends Number> Optional<T> min(String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }


        var anyNonNumbers = col.getEntries().stream().anyMatch(
                (value) -> !value.isNull() && !value.isLong() && !value.isDouble()
        );

        if (anyNonNumbers) {
            return Optional.empty();
            // error, can't apply max to non-numbers
        }

        var res = col.getEntries().stream().filter((value) -> value.getValue() != null).mapToDouble(
                (val) -> {
                    if (val.isLong()) {
                        return ((Long) val.getValue()).doubleValue();
                    } else {
                        return (Double) val.getValue();
                    }
                }).min();


        if (res.isEmpty()) {
            return Optional.empty();
        }

        return (Optional<T>) Optional.of(res.getAsDouble());
    }

    public <T extends Number> Optional<T> sum(String column) throws ColumnNotFoundException {
        var col = table.getColumn(column);

        if (col == null) {
            throw new ColumnNotFoundException(column);
        }

        var colEntries = col.getEntries();

        var nonNumbers = colEntries.stream().filter((value -> value.getValue() == null || (!value.isLong() && !value.isDouble())));

        if (nonNumbers.count() == colEntries.size()) {
            return Optional.empty();
        }

        var res = colEntries.stream().filter((value) -> value.getValue() != null && (value.isLong() || value.isDouble())).mapToDouble(
                (val) -> {
                    if (val.isLong()) {
                        return ((Long) val.getValue()).doubleValue();
                    } else {
                        return (Double) val.getValue();
                    }
                }).sum();

        return (Optional<T>) Optional.of(res);
    }

    public double mean(String column) {
        return 0.0;
    }

    public double std(String column) {
        return 0.0;
    }

    public double var(String column) {
        return 0.0;
    }
}
