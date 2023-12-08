package pt.up.fe.els2023.export;

import org.apache.commons.text.StringEscapeUtils;
import pt.up.fe.els2023.model.table.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HtmlExporter extends TableExporter {
    public static String DEFAULT_TITLE = "Table";
    public static String DEFAULT_STYLE = """
            table {
               border-collapse: collapse;
               width: 100%;
            }
            th, td {
               text-align: left;
               padding: 8px;
            }
            tr:nth-child(even){background-color: #f2f2f2}
            th {
               background-color: #4CAF50;
               color: white;
            }""";
    public static boolean DEFAULT_EXPORT_FULL_HTML = false;
    private final String title;
    private final String style;
    private final boolean exportFullHtml;

    public HtmlExporter(String filename, String path, String endOfLine, String title, String style, boolean exportFullHtml) {
        super(filename, path, endOfLine);
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

    public static Map<String, AttributeValue> getSupportedAttributes() {
        var attributes = new HashMap<String, AttributeValue>();

        attributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        attributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        attributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));
        attributes.put("title", new AttributeValue(AttributeValue.Type.STRING, DEFAULT_TITLE, false));
        attributes.put("style", new AttributeValue(AttributeValue.Type.STRING, DEFAULT_STYLE, false));
        attributes.put("exportFullHtml", new AttributeValue(AttributeValue.Type.BOOLEAN, DEFAULT_EXPORT_FULL_HTML, false));
        return attributes;
    }
}
