package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class WhereOperation extends TableOperation {

    protected final Predicate<RowWrapper> predicate;

    public WhereOperation(Predicate<RowWrapper> predicate) {
        this.predicate = predicate;
    }

    @Override
    public String getName() {
        return "Where( " + predicate + " )";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        Table newTable = new RacoonTable(table.getColumNames());

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

        return OperationResult.ofTable(newTable);
    }
}
