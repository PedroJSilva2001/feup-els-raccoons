package pt.up.fe.els2023.export;

import org.apache.commons.text.StringEscapeUtils;
import pt.up.fe.els2023.table.ITable;

import java.io.IOException;
import java.io.Writer;

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
    void export(Writer writer, ITable table) throws IOException {
        StringBuilder body = new StringBuilder(String.join(this.endOfLine,
                "<table>",
                "   <thead>",
                "   <tr>",
                ""));

        for (var column : table.getColumns()) {
            body.append("       <th>%s</th>".formatted(
                    StringEscapeUtils.escapeHtml4(column.getName())
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
                            StringEscapeUtils.escapeHtml4(value.toString())
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
}
