package pt.up.fe.els2023.export;

import org.apache.commons.text.StringEscapeUtils;
import pt.up.fe.els2023.table.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class HtmlExporter extends TableExporter {
    private final String title;
    private final String style;
    private final boolean exportFullHtml;

    public HtmlExporter(String table, String filename, String path, String endOfLine, String title, String style, boolean exportFullHtml) {
        super(table, filename, path, endOfLine);
        this.title = title;
        this.style = style.replaceAll("\\r\\n|\\n\\r|\\r|\\n", this.endOfLine);
        this.exportFullHtml = exportFullHtml;
    }

    private String escapeHtml(String string) {
        return StringEscapeUtils.escapeHtml4(string).replaceAll(" ", "&nbsp;");
    }

    private String buildTemplate(String title, String style, String body) {
        return String.join(this.endOfLine,
                "<!DOCTYPE html>",
                "<html>",
                "<head>",
                "   <meta charset=\"UTF-8\">",
                "   <title>%s</title>",
                "   <style>",
                "%s",
                "   </style>",
                "</head>",
                "<body>",
                "%s",
                "</body>",
                "</html>").formatted(title, style, body);
    }

    @Override
    void export(Writer writer, Table table) throws IOException {
        StringBuilder body = new StringBuilder(String.join(this.endOfLine,
                "<table style=\"white-space: pre-line;\">",
                "   <thead>",
                "   <tr>",
                ""));

        for (var column : table.getColumns()) {
            body.append("       <th>%s</th>".formatted(
                    escapeHtml(column.getName())
            ));
            body.append(this.endOfLine);
        }

        body.append(String.join(this.endOfLine,
                "   </tr>",
                "   </thead>",
                "   <tbody>",
                ""
        ));

        for (var row : table.getRows()) {
            body.append("   <tr>").append(this.endOfLine);

            for (var value : row.getValues()) {
                body.append("       <td>%s</td>".formatted(
                                escapeHtml(value.toString())
                        ))
                        .append(this.endOfLine);
            }

            body.append("   </tr>").append(this.endOfLine);
        }

        body.append(String.join(this.endOfLine,
                "   </tbody>",
                "</table>"
        ));

        if (exportFullHtml) {
            writer.write(buildTemplate(this.title, this.style, body.toString()));
        } else {
            writer.write(body.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HtmlExporter that)) return false;
        if (!super.equals(o)) return false;
        return exportFullHtml == that.exportFullHtml && Objects.equals(title, that.title) && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, style, exportFullHtml);
    }
}
