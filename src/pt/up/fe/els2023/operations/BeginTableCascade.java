package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return Optional.empty();
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
