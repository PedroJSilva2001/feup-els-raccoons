package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;

import java.util.function.Predicate;

public class DropWhereOperation extends WhereOperation {
    public DropWhereOperation(Predicate<RowWrapper> predicate) {
        super(predicate);
    }

    @Override
    public String getName() {
        return "DropWhere( " + predicate + " )";
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        return new WhereOperation(predicate.negate()).execute(table);
    }
}