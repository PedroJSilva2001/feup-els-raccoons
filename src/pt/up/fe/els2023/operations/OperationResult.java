package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.operations.TableCascade;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

public class OperationResult {
    private TableCascade btc;

    private Value value;

    public OperationResult(TableCascade btc) {
        this.btc = btc;
        this.value = null;
    }

    public OperationResult(Value value) {
        this.btc = null;
        this.value = value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    public void setTableCascade(TableCascade btc) {
        this.btc = btc;
    }

    public TableCascade getTableCascade() {
        return btc;
    }

    public Table getTable() {
        return btc.get();
    }

    public boolean hasValue() {
        return value != null;
    }

    public boolean hasTable() {
        return !hasValue() && btc != null;
    }
}
