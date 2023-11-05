package pt.up.fe.els2023.operations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameOperation implements TableOperation {

    private final List<String> columnNames;
    private final List<String> newColumnNames;

    public RenameOperation(List<String> columnNames, List<String> newColumnNames) {
        this.columnNames = columnNames;
        this.newColumnNames = newColumnNames;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) {
        btcInterpreter.apply(this);
    }

    public TableCascade execute(TableCascade btc) {
        Map<String, String> columnsMapping = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            columnsMapping.put(columnNames.get(i), newColumnNames.get(i));
        }
        return btc.rename(columnsMapping);
    }
}
