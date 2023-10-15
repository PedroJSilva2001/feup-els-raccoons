package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.Row;
import pt.up.fe.els2023.table.Value;

import java.util.ArrayList;

public class EqualsPredicate extends Predicate {
    private final String column;
    private final Value value;

    public EqualsPredicate(String column, Value value) {
        this.column = column;
        this.value = value;
    }

    @Override
    protected boolean test(Row row) {
        return false;
    }
}
