package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.util.HashMap;

public class ExportOperation implements TableOperation {

    private final TableExporter exporter;

    public ExportOperation(TableExporter exporter) {
        this.exporter = exporter;
    }

    public void accept(BTCinterpreter btcInterpreter) throws IOException, TableNotFoundException {
        btcInterpreter.apply(this);
    }

    public void execute(BeginTableCascade btc) throws TableNotFoundException, IOException {
        HashMap<String, ITable> map = new HashMap<>();
        map.put(exporter.getTable(), btc.get());
        exporter.export(map);
    }
}
