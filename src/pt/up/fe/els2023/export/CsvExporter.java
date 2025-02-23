package pt.up.fe.els2023.export;

import pt.up.fe.els2023.model.table.Column;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Row;
import pt.up.fe.els2023.model.table.Value;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CsvExporter extends TableExporter {
    public static String DEFAULT_SEPARATOR = ",";
    private final String separator;

    public CsvExporter(String filename, String path, String endOfLine, String separator) {
        super(filename, path, endOfLine);
        this.separator = separator;
    }

    private boolean stringContainsNewLine(String value) {
        return value.contains("\n") || value.contains("\r");
    }

    private String escapeSeparatorAndNewLine(String value) {
        if (value.contains(separator) || stringContainsNewLine(value)) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        } else {
            return value;
        }
    }

    @Override
    void export(Writer writer, Table table) throws IOException {
        List<Column> columns = table.getColumns();

        if (columns.isEmpty()) {
            return;
        }

        writer.write(escapeSeparatorAndNewLine(columns.get(0).getName()));

        for (int i = 1; i < columns.size(); i++) {
            writer.write(separator);
            writer.write(escapeSeparatorAndNewLine(columns.get(i).getName()));
        }

        writer.write(endOfLine);

        for (Row row : table.getRows()) {
            List<Value> values = row.getValues();

            if (!values.isEmpty()) {
                writer.write(escapeSeparatorAndNewLine(values.get(0).toString()));
            }

            for (int i = 1; i < values.size(); i++) {
                writer.write(separator);
                writer.write(escapeSeparatorAndNewLine(values.get(i).toString()));
            }

            writer.write(endOfLine);
        }

        writer.flush();
    }

    public String getSeparator() {
        return separator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CsvExporter that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(separator, that.separator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), separator);
    }

    public static Map<String, AttributeValue> getSupportedAttributes() {
        var attributes = new HashMap<String, AttributeValue>();

        attributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        attributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        attributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));
        attributes.put("separator", new AttributeValue(AttributeValue.Type.STRING, DEFAULT_SEPARATOR, false));

        return attributes;
    }
}
