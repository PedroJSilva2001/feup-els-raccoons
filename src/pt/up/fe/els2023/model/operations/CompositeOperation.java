package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.List;

public class CompositeOperation extends TableOperation {

    private final List<TableOperation> operations;

    public CompositeOperation(List<TableOperation> operations) {
        this.operations = operations;
    }

    public CompositeOperation() {
        this.operations = new ArrayList<>();
    }

    public void addOperation(TableOperation operation) {
        operations.add(operation);
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder("CompositeOperation[");

        for (var operation : operations) {
            name.append(operation.getName()).append(" -> ");
        }

        name.append(" ]");

        return name.toString();

    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        assert table != null;

        var result = OperationResult.ofTable(new RacoonTable(table));

        if (operations.isEmpty()) {
            return result;
        }

        for (int i = 0; i < operations.size(); i++) {

            var operation = operations.get(i);

            if (i != operations.size() - 1 && operation.isTerminal()) {
                throw new ImproperTerminalOperationException(operation.getName());
            }

            result = operation.execute(result.getTable());
        }

        return result;
    }
}
