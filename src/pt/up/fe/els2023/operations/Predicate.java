package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.Row;

import java.util.Map;

public abstract class Predicate {
    protected abstract boolean test(Row row);
}
