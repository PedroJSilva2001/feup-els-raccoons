package pt.up.fe.els2023.model.operations;

import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.table.Table;

import java.io.IOException;

public class ExportOperation extends TableOperation {

    private final TableExporter exporter;

    public ExportOperation(TableExporter exporter) {
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
    public OperationResult execute(Table table) throws ColumnNotFoundException {
        try {
            exporter.export(table);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return OperationResult.ofValue(null);
    }
}
