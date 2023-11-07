package pt.up.fe.els2023.interpreter.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.operations.TableCascade;

import java.io.IOException;

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
        exporter.export(table);

        return null;
    }
}
