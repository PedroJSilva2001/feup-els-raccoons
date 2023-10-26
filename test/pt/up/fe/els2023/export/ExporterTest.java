package pt.up.fe.els2023.export;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.up.fe.els2023.table.Column;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Row;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExporterTest {
    ITable table;


    @BeforeEach
    public void init() {
        table = Mockito.mock(ITable.class);
        Mockito.when(table.getName()).thenReturn("table1");

        Column column = new Column("data");
        column.addEntry(Value.of(1));
        column.addEntry(Value.of(2));
        column.addEntry(Value.of(3));
        column.addEntry(Value.of(4));
        column.addEntry(Value.ofNull());
        column.addEntry(Value.of(1));

        Column column2 = new Column("strings");
        column.addEntry(Value.of("stuff"));
        column.addEntry(Value.of("things"));
        column.addEntry(Value.of("zau"));
        column.addEntry(Value.of("ano\\ther & one"));
        column.addEntry(Value.of("multip\"le;spaces  ah"));
        column.addEntry(Value.of("  inner\r\n  "));
        column.addEntry(Value.of(" extra<!-- row"));

        Column column3 = new Column("doubles");
        column.addEntry(Value.of(1.03));
        column.addEntry(Value.of(2.3));
        column.addEntry(Value.of(3.10345));
        column.addEntry(Value.of(4.0));
        column.addEntry(Value.of(5.123));
        column.addEntry(Value.of(1));
        column.addEntry(Value.of("&nbsp;"));

        List<Column> columns = List.of(column, column2, column3);
        Mockito.when(table.getColumns()).thenReturn(columns);

        Row row1 = new Row(List.of(Value.of(1), Value.of("stuff"), Value.of(1.03)));
        Row row2 = new Row(List.of(Value.of(2), Value.of("things"), Value.of(2.3)));
        Row row3 = new Row(List.of(Value.of(3), Value.of("zau"), Value.of(3.10345)));
        Row row4 = new Row(List.of(Value.of(4), Value.of("ano\\ther & one"), Value.of(4.0)));
        Row row5 = new Row(Stream.of(Value.ofNull(), Value.of("multip\"le;spaces  ah"), Value.of(5.123))
                .collect(Collectors.toList()));
        Row row6 = new Row(List.of(Value.of(1), Value.of("  inner\r\n  "), Value.of(1)));
        Row row7 = new Row(Stream.of(Value.ofNull(), Value.of(" extra<!-- row"), Value.of("&nbsp;")).collect(Collectors.toList()));

        Mockito.when(table.getRows()).thenReturn(List.of(
                row1, row2, row3, row4, row5, row6, row7
        ));
    }

    @Test
    public void exportCsv() throws IOException {
        CsvExporter exporter = new CsvExporter("table1", "", "", "\r\n", ";");
        StringWriter writer = new StringWriter();

        exporter.export(writer, table);

        Scanner scanner = new Scanner(writer.toString()).useDelimiter("\r\n");
        AtomicReference<String> firstString = new AtomicReference<>("");
        Assertions.assertDoesNotThrow(() -> firstString.set(scanner.next()));
        Assertions.assertEquals("data;strings;doubles", firstString.get());
        Assertions.assertEquals("1;stuff;1.03", scanner.next());
        Assertions.assertEquals("2;things;2.3", scanner.next());
        Assertions.assertEquals("3;zau;3.10345", scanner.next());
        Assertions.assertEquals("4;ano\\ther & one;4.0", scanner.next());
        Assertions.assertEquals(";\"multip\"\"le;spaces  ah\";5.123", scanner.next());
        Assertions.assertEquals("1;\"  inner", scanner.next());
        Assertions.assertEquals("  \";1", scanner.next());
        Assertions.assertEquals("; extra<!-- row;\"&nbsp;\"", scanner.next());
        Assertions.assertFalse(scanner.hasNext());
    }

    @Test
    public void exportTsv() throws IOException {
        TsvExporter exporter = new TsvExporter("table1", "", "", "\r\n");
        StringWriter writer = new StringWriter();

        exporter.export(writer, table);

        Scanner scanner = new Scanner(writer.toString()).useDelimiter("\r\n");
        AtomicReference<String> firstString = new AtomicReference<>("");
        Assertions.assertDoesNotThrow(() -> firstString.set(scanner.next()));
        Assertions.assertEquals("data\tstrings\tdoubles", firstString.get());
        Assertions.assertEquals("1\tstuff\t1.03", scanner.next());
        Assertions.assertEquals("2\tthings\t2.3", scanner.next());
        Assertions.assertEquals("3\tzau\t3.10345", scanner.next());
        Assertions.assertEquals("4\tano\\ther & one\t4.0", scanner.next());
        Assertions.assertEquals("\tmultip\"le;spaces  ah\t5.123", scanner.next());
        Assertions.assertEquals("1\t\"  inner", scanner.next());
        Assertions.assertEquals("  \"\t1", scanner.next());
        Assertions.assertEquals("\t extra<!-- row\t&nbsp;", scanner.next());
        Assertions.assertFalse(scanner.hasNext());
    }

    @Test
    public void exportHtml() {
        HtmlExporter exporter = new HtmlExporter("table1", "", "", "\n\r", "Table", """
                table {
                   border-collapse: collapse;
                   width: 100%;
                }""", true);

        StringWriter writer = new StringWriter();
        Assertions.assertDoesNotThrow(() -> exporter.export(writer, table));

        Scanner scanner = new Scanner(writer.toString()).useDelimiter("\n\r");
        AtomicReference<String> firstString = new AtomicReference<>("");
        Assertions.assertDoesNotThrow(() -> firstString.set(scanner.next()));

        Assertions.assertEquals("<!DOCTYPE html>", firstString.get());
        Assertions.assertEquals("<html>", scanner.next());
        Assertions.assertEquals("<head>", scanner.next());
        Assertions.assertEquals("   <meta charset=\"UTF-8\">", scanner.next());
        Assertions.assertEquals("   <title>Table</title>", scanner.next());
        Assertions.assertEquals("   <style>", scanner.next());
        Assertions.assertEquals("table {", scanner.next());
        Assertions.assertEquals("   border-collapse: collapse;", scanner.next());
        Assertions.assertEquals("   width: 100%;", scanner.next());
        Assertions.assertEquals("}", scanner.next());
        Assertions.assertEquals("   </style>", scanner.next());
        Assertions.assertEquals("</head>", scanner.next());
        Assertions.assertEquals("<body>", scanner.next());
        Assertions.assertEquals("<table style=\"white-space: pre-line;\">", scanner.next());
        Assertions.assertEquals("   <thead>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <th>data</th>", scanner.next());
        Assertions.assertEquals("       <th>strings</th>", scanner.next());
        Assertions.assertEquals("       <th>doubles</th>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   </thead>", scanner.next());
        Assertions.assertEquals("   <tbody>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>1</td>", scanner.next());
        Assertions.assertEquals("       <td>stuff</td>", scanner.next());
        Assertions.assertEquals("       <td>1.03</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>2</td>", scanner.next());
        Assertions.assertEquals("       <td>things</td>", scanner.next());
        Assertions.assertEquals("       <td>2.3</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>3</td>", scanner.next());
        Assertions.assertEquals("       <td>zau</td>", scanner.next());
        Assertions.assertEquals("       <td>3.10345</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>4</td>", scanner.next());
        Assertions.assertEquals("       <td>ano\\ther&nbsp;&amp;&nbsp;one</td>", scanner.next());
        Assertions.assertEquals("       <td>4.0</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td></td>", scanner.next());
        Assertions.assertEquals("       <td>multip&quot;le;spaces&nbsp;&nbsp;ah</td>", scanner.next());
        Assertions.assertEquals("       <td>5.123</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>1</td>", scanner.next());
        Assertions.assertEquals("       <td>&nbsp;&nbsp;inner\r\n&nbsp;&nbsp;</td>", scanner.next());
        Assertions.assertEquals("       <td>1</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td></td>", scanner.next());
        Assertions.assertEquals("       <td>&nbsp;extra&lt;!--&nbsp;row</td>", scanner.next());
        Assertions.assertEquals("       <td>&amp;nbsp;</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   </tbody>", scanner.next());
        Assertions.assertEquals("</table>", scanner.next());
        Assertions.assertEquals("</body>", scanner.next());
        Assertions.assertEquals("</html>", scanner.next());
        Assertions.assertFalse(scanner.hasNext());
    }

    @Test
    public void exportTableHtml() {
        HtmlExporter exporter = new HtmlExporter("table1", "", "", "\n", "Table",
                "", false);

        StringWriter writer = new StringWriter();
        Assertions.assertDoesNotThrow(() -> exporter.export(writer, table));

        Scanner scanner = new Scanner(writer.toString()).useDelimiter("\n");
        AtomicReference<String> firstString = new AtomicReference<>("");
        Assertions.assertDoesNotThrow(() -> firstString.set(scanner.next()));

        Assertions.assertEquals("<table style=\"white-space: pre-line;\">", firstString.get());
        Assertions.assertEquals("   <thead>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <th>data</th>", scanner.next());
        Assertions.assertEquals("       <th>strings</th>", scanner.next());
        Assertions.assertEquals("       <th>doubles</th>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   </thead>", scanner.next());
        Assertions.assertEquals("   <tbody>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>1</td>", scanner.next());
        Assertions.assertEquals("       <td>stuff</td>", scanner.next());
        Assertions.assertEquals("       <td>1.03</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>2</td>", scanner.next());
        Assertions.assertEquals("       <td>things</td>", scanner.next());
        Assertions.assertEquals("       <td>2.3</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>3</td>", scanner.next());
        Assertions.assertEquals("       <td>zau</td>", scanner.next());
        Assertions.assertEquals("       <td>3.10345</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>4</td>", scanner.next());
        Assertions.assertEquals("       <td>ano\\ther&nbsp;&amp;&nbsp;one</td>", scanner.next());
        Assertions.assertEquals("       <td>4.0</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td></td>", scanner.next());
        Assertions.assertEquals("       <td>multip&quot;le;spaces&nbsp;&nbsp;ah</td>", scanner.next());
        Assertions.assertEquals("       <td>5.123</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td>1</td>", scanner.next());
        Assertions.assertEquals("       <td>&nbsp;&nbsp;inner\r", scanner.next());
        Assertions.assertEquals("&nbsp;&nbsp;</td>", scanner.next());
        Assertions.assertEquals("       <td>1</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   <tr>", scanner.next());
        Assertions.assertEquals("       <td></td>", scanner.next());
        Assertions.assertEquals("       <td>&nbsp;extra&lt;!--&nbsp;row</td>", scanner.next());
        Assertions.assertEquals("       <td>&amp;nbsp;</td>", scanner.next());
        Assertions.assertEquals("   </tr>", scanner.next());
        Assertions.assertEquals("   </tbody>", scanner.next());
        Assertions.assertEquals("</table>", scanner.next());
        Assertions.assertFalse(scanner.hasNext());
    }

    @Test
    public void exportLatex() {
        LatexExporter exporter = new LatexExporter("table1", "", "", "\n");

        StringWriter writer = new StringWriter();
        Assertions.assertDoesNotThrow(() -> exporter.export(writer, table));

        Scanner scanner = new Scanner(writer.toString()).useDelimiter("\n");
        AtomicReference<String> firstString = new AtomicReference<>("");
        Assertions.assertDoesNotThrow(() -> firstString.set(scanner.next()));

        Assertions.assertEquals("\\begin{center}", firstString.get());
        Assertions.assertEquals("\\begin{tabular}{ | l | l | l | }", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("data & strings & doubles \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("1 & stuff & 1.03 \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("2 & things & 2.3 \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("3 & zau & 3.10345 \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("4 & ano\\textbackslash ther \\& one & 4.0 \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals(" & multip\"le;spaces  ah & 5.123 \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("1 &   inner\r", scanner.next());
        Assertions.assertEquals("   & 1 \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals(" &  extra<!-- row & \\&nbsp; \\\\", scanner.next());
        Assertions.assertEquals("\\hline", scanner.next());
        Assertions.assertEquals("\\end{tabular}", scanner.next());
        Assertions.assertEquals("\\end{center}", scanner.next());
        Assertions.assertFalse(scanner.hasNext());
    }

    @Test
    public void exportMarkdown() {
        MarkdownExporter markdownExporter = new MarkdownExporter("table1", "", "", "\n");

        StringWriter writer = new StringWriter();
        Assertions.assertDoesNotThrow(() -> markdownExporter.export(writer, table));

        Scanner scanner = new Scanner(writer.toString()).useDelimiter("\n");
        AtomicReference<String> firstString = new AtomicReference<>("");
        Assertions.assertDoesNotThrow(() -> firstString.set(scanner.next()));

        Assertions.assertEquals("| data | strings                             | doubles    |", firstString.get());
        Assertions.assertEquals("|------|-------------------------------------|------------|", scanner.next());
        Assertions.assertEquals("| 1    | stuff                               | 1\\.03      |", scanner.next());
        Assertions.assertEquals("| 2    | things                              | 2\\.3       |", scanner.next());
        Assertions.assertEquals("| 3    | zau                                 | 3\\.10345   |", scanner.next());
        Assertions.assertEquals("| 4    | ano\\\\ther&nbsp;&amp;&nbsp;one       | 4\\.0       |", scanner.next());
        Assertions.assertEquals("|      | multip&quot;le;spaces&nbsp;&nbsp;ah | 5\\.123     |", scanner.next());
        Assertions.assertEquals("| 1    | &nbsp;&nbsp;inner<br />&nbsp;&nbsp; | 1          |", scanner.next());
        Assertions.assertEquals("|      | &nbsp;extra&lt;\\!\\-\\-&nbsp;row      | &amp;nbsp; |", scanner.next());
    }
}
