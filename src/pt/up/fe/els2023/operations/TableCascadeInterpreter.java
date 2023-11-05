package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TableCascadeInterpreter {
    private TableCascade btc;
    private Value valueResult;
    private final Map<String, ITable> tables;
    private final Map<String, Value> resultVariables;

    public TableCascadeInterpreter(TableCascade btc, Map<String, ITable> tables, Map<String, Value> resultVariables) {
        this.btc = btc;
        this.tables = tables;
        this.resultVariables = resultVariables;
    }

    public void apply(ArgMaxOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(ArgMinOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(ConcatHorizontalOperation operation) {
        btc = operation.execute(btc, getAdditionalTablesByName(operation.additionalTableNames()));
    }

    public void apply(ConcatVerticalOperation operation) {
        btc = operation.execute(btc, getAdditionalTablesByName(operation.additionalTableNames()));
    }

    public void apply(SelectOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(RejectOperation operation) throws ColumnNotFoundException {
        btc = operation.execute(btc);
    }

    public void apply(ExportOperation operation) throws TableNotFoundException, IOException {
        operation.execute(btc);
    }

    public void apply(WhereOperation operation) {
        btc = operation.execute(btc, resultVariables);
    }

    public void apply(DropWhereOperation operation) {
        btc = operation.execute(btc, resultVariables);
    }

    public void apply(MaxOperation operation) throws ColumnNotFoundException {
        operation.execute(btc).ifPresent(value -> valueResult = value);
    }

    public void apply(MinOperation operation) throws ColumnNotFoundException {
        operation.execute(btc).ifPresent(value -> valueResult = value);
    }

    public void apply(CountOperation operation) throws ColumnNotFoundException {
        valueResult = Value.of(operation.execute(btc));
    }

    public void apply(SumOperation operation) throws ColumnNotFoundException {
        operation.execute(btc).ifPresent(value -> valueResult = value);
    }

    public void apply(MeanOperation operation) throws ColumnNotFoundException {
        operation.execute(btc).ifPresent(value -> valueResult = value);
    }

    public Value getValueResult() {
        return valueResult;
    }

    public TableCascade getBtc() {
        return btc;
    }

    private ITable[] getAdditionalTablesByName(List<String> additionalTableNames) {
        var additionalTables = new ITable[additionalTableNames.size()];
        for (int i = 0; i < additionalTableNames.size(); i++) {
            additionalTables[i] = tables.get(additionalTableNames.get(i));
        }
        return additionalTables;
    }
}
