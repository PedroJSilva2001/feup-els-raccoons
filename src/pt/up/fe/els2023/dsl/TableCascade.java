package pt.up.fe.els2023.dsl;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableCascadeAlreadyConsumedException;
import pt.up.fe.els2023.model.operations.*;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.*;
import java.util.function.Predicate;

public class TableCascade {
    private final Table table;

    private final CompositeOperation compositeOperation;

    private boolean consumed;

    public TableCascade(Table table) {
        this.table = table;
        this.compositeOperation = new CompositeOperation();
        this.consumed = false;
    }

    public Table get() throws ColumnNotFoundException, TableCascadeAlreadyConsumedException {
        if (consumed) {
            throw new TableCascadeAlreadyConsumedException(table.getName());
        }

        this.consumed = true;

        try {
            return compositeOperation.execute(table).getTable();
        } catch (ImproperTerminalOperationException ignored) { }

        return null;
    }

    public TableCascade join(Table other, String column) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new JoinOperation(other, column));
    }

    public TableCascade select(String ...columns) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new SelectOperation(List.of(columns)));
    }

    public TableCascade reject(String ...columns) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new RejectOperation(List.of(columns)));
    }

    public TableCascade where(Predicate<RowWrapper> predicate) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new WhereOperation(predicate));
    }

    public TableCascade dropWhere(Predicate<RowWrapper> predicate) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new DropWhereOperation(predicate));
    }

    public TableCascade concatVertical(Table...others) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new ConcatVerticalOperation(List.of(others)));
    }

    public TableCascade concatHorizontal(Table...others) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new ConcatHorizontalOperation(List.of(others)));
    }

    public TableCascade rename(Map<String, String> columnMapping) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new RenameOperation(columnMapping));
    }

    public TableCascade sort(String column, boolean ascending) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new SortOperation(column, ascending));
    }

    public TableCascade sort(String column) throws TableCascadeAlreadyConsumedException {
        return sort(column, true);
    }

    public Value count(String column) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getSingleAggregateResult(new CountOperation(List.of(column)));
    }

    public Map<String, Value> count(String ...columns) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getMultipleAggregateResults(new CountOperation(List.of(columns)));
    }

    public Value max(String column) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getSingleAggregateResult(new MaxOperation(List.of(column)));
    }

    public Map<String, Value> max(String ...columns) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getMultipleAggregateResults(new MaxOperation(List.of(columns)));
    }

    public TableCascade argMax(String column) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new ArgMaxOperation(column));
    }

    public Value min(String column) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getSingleAggregateResult(new MinOperation(List.of(column)));
    }

    public Map<String, Value> min(String ...columns) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getMultipleAggregateResults(new MinOperation(List.of(columns)));
    }

    public TableCascade argMin(String column) throws TableCascadeAlreadyConsumedException {
        return addNonAggregateOperation(new ArgMinOperation(column));
    }

    public Value sum(String column) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getSingleAggregateResult(new SumOperation(List.of(column)));
    }

    public Map<String, Value> sum(String ...columns) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getMultipleAggregateResults(new SumOperation(List.of(columns)));
    }


    public Value mean(String column) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getSingleAggregateResult(new MeanOperation(List.of(column)));
    }

    public Map<String, Value> mean(String ...columns) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        return getMultipleAggregateResults(new MeanOperation(List.of(columns)));
    }

    // TODO: implement
    public Value std(String column) throws TableCascadeAlreadyConsumedException {
        return null;
    }

    // TODO: implement
    public Map<String, Value> std(String ...columns) throws TableCascadeAlreadyConsumedException {
        return null;
    }

    // TODO: implement
    public Value var(String column) throws TableCascadeAlreadyConsumedException {
        return null;
    }

    // TODO: implement
    public Map<String, Optional<Value>> var(String ...columns) throws TableCascadeAlreadyConsumedException {
        return null;
    }

    private TableCascade addNonAggregateOperation(TableOperation operation) throws TableCascadeAlreadyConsumedException {
        if (consumed) {
            throw new TableCascadeAlreadyConsumedException(table.getName());
        }

        compositeOperation.addOperation(operation);
        return this;
    }

    private Value getSingleAggregateResult(TableOperation operation) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        if (consumed) {
            throw new TableCascadeAlreadyConsumedException(table.getName());
        }

        consumed = true;

        compositeOperation.addOperation(operation);

        try {
            return compositeOperation.execute(table).getValue();
        } catch (ImproperTerminalOperationException ignored) { }

        return null;
    }

    private Map<String, Value> getMultipleAggregateResults(TableOperation operation) throws TableCascadeAlreadyConsumedException, ColumnNotFoundException {
        if (consumed) {
            throw new TableCascadeAlreadyConsumedException(table.getName());
        }

        consumed = true;


        compositeOperation.addOperation(operation);

        try {
            return compositeOperation.execute(table).getValueMap();
        } catch (ImproperTerminalOperationException ignored) { }

        return null;
    }
}
