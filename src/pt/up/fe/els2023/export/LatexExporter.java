package pt.up.fe.els2023.export;

import pt.up.fe.els2023.table.Table;

import java.io.IOException;
import java.io.Writer;

public class LatexExporter extends TableExporter {
    public LatexExporter(String filename, String path, String endOfLine) {
        super(filename, path, endOfLine);
    }

    private String escapeLatex(String string) {
        return string.replaceAll("\\\\", "\\\\textbackslash ")
                .replaceAll("~", "\\\\textasciitilde ")
                .replaceAll("\\^", "\\\\textasciicircum ")
                .replaceAll("([{}$&_#%])", "\\\\$1");
    }

    @Override
    void export(Writer writer, Table table) throws IOException {
        StringBuilder sb = new StringBuilder(String.join(endOfLine,
                "\\begin{center}",
                "\\begin{tabular}{ |" + " l |".repeat(table.getColumns().size()) + " }",
                "\\hline", ""));

        boolean first = true;
        for (var column : table.getColumns()) {
            if (first) {
                first = false;
            } else {
                sb.append(" & ");
            }
            sb.append(
                    escapeLatex(column.getName())
            );
        }
        sb.append(String.join(endOfLine,
                " \\\\",
                "\\hline",
                "\\hline",
                ""));

        for (var row : table.getRows()) {
            first = true;
            for (var value : row.getValues()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" & ");
                }
                sb.append(
                        escapeLatex(value.toString())
                );
            }
            sb.append(String.join(endOfLine,
                    " \\\\",
                    "\\hline",
                    ""));
        }

        sb.append(String.join(endOfLine,
                "\\end{tabular}",
                "\\end{center}"));

        writer.write(sb.toString());
    }
}
