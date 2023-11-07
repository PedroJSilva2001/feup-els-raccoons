package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.Table;

import java.io.IOException;
import java.util.HashMap;

public class ExportOperation extends TableOperation {

    private final TableExporter exporter;

    public ExportOperation(String initialTable, String resultVariableName, TableExporter exporter) {
        super(initialTable, resultVariableName);
        this.exporter = exporter;
    }

    @Override
    public String getName() {
        return "Export( " + exporter.toString() + " )";
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public OperationResult execute(TableCascade tableCascade, VariablesTable variablesTable) throws ColumnNotFoundException, TableNotFoundException, IOException, ImproperTerminalOperationException {

        var table = tableCascade.get();

        HashMap<String, Table> tables = new HashMap<>();

        tables.put(exporter.getTable(), table);

        exporter.export(tables);

        return null;
    }
}
