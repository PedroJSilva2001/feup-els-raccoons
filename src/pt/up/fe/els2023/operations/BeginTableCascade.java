package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;

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
            Map<String, Object> mapping = new HashMap<>();

            for (int i = 0; i < row.getValues().size(); i++) {
                mapping.put(table.getColumn(i).getName(), row.get(i).value());
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
                (value) -> value.value() != null
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

        var res = col.getEntries().stream().filter((value) -> value.value() != null).mapToDouble(
                (val) -> {
                    if (val.isLong()) {
                        return ((Long) val.value()).doubleValue();
                    } else {
                        return (Double) val.value();
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

        var res = col.getEntries().stream().filter((value) -> value.value() != null).mapToDouble(
                (val) -> {
                    if (val.isLong()) {
                        return ((Long) val.value()).doubleValue();
                    } else {
                        return (Double) val.value();
                    }
                }).min();


        if (res.isEmpty()) {
            return Optional.empty();
        }

        return (Optional<T>) Optional.of(res.getAsDouble());
    }

    public <T extends Number> T sum(String column) {
        return null;
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

    public static void main(String[] args) throws ColumnNotFoundException {
        ITable table = new Table();
        table.addColumn("Col1");
        table.addColumn("Col2");

        table.addRow(List.of("", Long.valueOf(1), "hello"));
        table.addRow(List.of("", Long.valueOf(2), "bye"));
        table.addRow(List.of("", Long.valueOf(3), ""));
        var r1 = new ArrayList<>();
        r1.add(""); r1.add(Long.valueOf(4)); r1.add(null);
        table.addRow(r1);

        var r2 = new ArrayList<>();
        r2.add(""); r2.add(Long.valueOf(6)); r2.add(null);
        table.addRow(r2);

        BeginTableCascade btc = new BeginTableCascade(table);

        var newTable = btc.dropWhere(
                (row) -> row.get("Col1").equals(1)
        ).get();

        System.out.println(btc.count("Col2"));

        System.out.println(btc.max("Col1").get());

        System.out.println(btc.min("Col1").get());

    }
}
