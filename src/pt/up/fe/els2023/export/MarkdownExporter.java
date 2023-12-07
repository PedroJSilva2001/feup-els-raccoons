package pt.up.fe.els2023.export;

import org.apache.commons.text.StringEscapeUtils;
import pt.up.fe.els2023.model.table.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class MarkdownExporter extends TableExporter {
    public MarkdownExporter(String filename, String path, String endOfLine) {
        super(filename, path, endOfLine);
    }

    private String escapeMarkdown(String string) {
        string = StringEscapeUtils.escapeHtml4(string);
        return string.replaceAll("([\\\\`*_{}\\[\\]()#+\\-.!<>])", "\\\\$1")
                .replaceAll("\\|", "&#124;")
                .replaceAll(" ", "&nbsp;")
                .replaceAll("\\r\\n|\\n\\r|\\r|\\n", "<br />");
    }

    private List<Integer> getMaxColumnLength(List<String> columns, List<List<String>> rows) {
        List<Integer> maxColumnLength = new java.util.ArrayList<>(columns.stream().map(
                String::length
        ).toList());

        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxColumnLength.set(i, Math.max(maxColumnLength.get(i), row.get(i).length()));
            }
        }

        return maxColumnLength;
    }

    private void buildRow(List<Integer> maxColumnLength, StringBuilder stringBuilder, List<String> row) {
        if (row.isEmpty()) {
            return;
        }

        stringBuilder.append("|");
        for (int i = 0; i < row.size(); i++) {
            stringBuilder.append(" ");
            stringBuilder.append(
                    row.get(i)
            );
            stringBuilder.append(" ".repeat(maxColumnLength.get(i) - row.get(i).length()));
            stringBuilder.append(" |");
        }
        stringBuilder.append(endOfLine);
    }

    private List<String> normalizeColumns(Table table) {
        return table.getColumns().stream().map(
                        column -> escapeMarkdown(column.getName()))
                .toList();
    }

    private List<List<String>> normalizeRows(Table table) {
        return table.getRows().stream().map(
                        row -> row.getValues().stream().map(
                                        value -> escapeMarkdown(value.toString()))
                                .toList())
                .toList();
    }

    @Override
    void export(Writer writer, Table table) throws IOException {
        if (table.getColumns().isEmpty()) {
            return;
        }

        List<String> columns = normalizeColumns(table);
        List<List<String>> rows = normalizeRows(table);
        List<Integer> maxColumnLength = getMaxColumnLength(columns, rows);

        StringBuilder stringBuilder = new StringBuilder();
        buildRow(maxColumnLength, stringBuilder, columns);

        for (int i = 0; i < columns.size(); i++) {
            stringBuilder.append("|-");
            stringBuilder.append(
                    "-".repeat(maxColumnLength.get(i))
            );
            stringBuilder.append("-");
        }
        stringBuilder.append("|");
        stringBuilder.append(endOfLine);

        for (List<String> row : rows) {
            buildRow(maxColumnLength, stringBuilder, row);
        }

        writer.write(stringBuilder.toString());
    }
}
