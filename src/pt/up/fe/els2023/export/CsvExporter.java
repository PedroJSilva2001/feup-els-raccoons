package pt.up.fe.els2023.export;

import pt.up.fe.els2023.Config;
import pt.up.fe.els2023.table.Column;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Row;
import pt.up.fe.els2023.utils.ValueWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CsvExporter extends TableExporter {
    private final String separator;

    public CsvExporter(String table, String filename, String path, String endOfLine, String separator) {
        super(table, filename, path, endOfLine);
        this.separator = separator;
    }

    @Override
    void export(Writer writer, ITable table) throws IOException {
        List<Column> columns = table.getColumns();

        if (columns.isEmpty()) {
            return;
        }

        writer.write(columns.get(0).getName());

        for (int i = 1; i < columns.size(); i++) {
            writer.write(separator);
            writer.write(columns.get(i).getName());
        }

        writer.write(endOfLine);

        for (Row row : table.getRows()) {
            List<Object> values = row.getValues();

            if (!values.isEmpty()) {
                writer.write(ValueWriter.write(values.get(0)));
            }

            for (int i = 1; i < values.size(); i++) {
                writer.write(separator);
                writer.write(ValueWriter.write(values.get(i)));
            }

            writer.write(endOfLine);
        }

        writer.flush();
    }

    public String getSeparator() {
        return separator;
    }
}
