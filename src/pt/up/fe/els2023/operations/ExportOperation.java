package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.HashMap;

public class ExportOperation implements TableOperation {

    private final TableExporter exporter;

    public ExportOperation(TableExporter exporter) {
        this.exporter = exporter;
    }

    @Override
    public String name() {
        return "Export( " + exporter.toString() + " )";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public OperationResult execute(OperationResult previousResult, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {

        var table = previousResult.getTableCascade().get();

        HashMap<String, ITable> tables = new HashMap<>();

        tables.put(exporter.getTable(), table);

        exporter.export(tables);

        return null;
    }

    @Override
    public OperationResult execute(VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {
        return null;
    }
}
