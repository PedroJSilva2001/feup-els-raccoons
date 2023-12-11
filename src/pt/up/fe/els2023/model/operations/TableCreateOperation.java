package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.schema.TableSchema;
import pt.up.fe.els2023.model.table.Table;

public class TableCreateOperation extends TableOperation {
    private final TableSchema tableSchema;

    public TableCreateOperation(TableSchema tableSchema) {
        this.tableSchema = tableSchema;
    }

    @Override
    public String getName() {
        return "TableCreate(" + tableSchema.name() + ")";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public OperationResult execute(Table table) throws ColumnNotFoundException, ImproperTerminalOperationException {
        return OperationResult.ofTable(tableSchema.collect());
    }
}
