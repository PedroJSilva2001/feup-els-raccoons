package pt.up.fe.els2023.interpreter.signatures;

import pt.up.fe.els2023.export.CsvExporter;
import pt.up.fe.els2023.export.HtmlExporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signatures {
    public record AttributeValue(Type type, Object defaultValue, boolean required) {
        public enum Type {
            STRING,
            BOOLEAN,
            EXPRESSION,
            TABLE,
            VARIADIC_STRING,
            VARIADIC_TABLE,
            STRING_MAP,
        }
    }


    public static final Map<String, List<AttributeValue>> operationSignatures;
    public static final Map<String, Map<String, AttributeValue>> exportSignatures;

    static {
        operationSignatures = new HashMap<>();

        var joinSignature = new ArrayList<AttributeValue>();
        joinSignature.add(new AttributeValue(AttributeValue.Type.TABLE, null, true));
        joinSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("join", joinSignature);

        var selectSignature = new ArrayList<AttributeValue>();
        selectSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("select", selectSignature);

        var rejectSignature = new ArrayList<AttributeValue>();
        rejectSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("reject", rejectSignature);

        var whereSignature = new ArrayList<AttributeValue>();
        whereSignature.add(new AttributeValue(AttributeValue.Type.EXPRESSION, null, true));

        operationSignatures.put("where", whereSignature);

        var dropWhereSignature = new ArrayList<AttributeValue>();
        dropWhereSignature.add(new AttributeValue(AttributeValue.Type.EXPRESSION, null, true));

        operationSignatures.put("dropWhere", dropWhereSignature);

        var concatVerticalSignature = new ArrayList<AttributeValue>();
        concatVerticalSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_TABLE, List.of(), false));

        operationSignatures.put("concatVertical", concatVerticalSignature);

        var concatHorizontalSignature = new ArrayList<AttributeValue>();
        concatHorizontalSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_TABLE, List.of(), false));

        operationSignatures.put("concatHorizontal", concatHorizontalSignature);

        var renameSignature = new ArrayList<AttributeValue>();
        renameSignature.add(new AttributeValue(AttributeValue.Type.STRING_MAP, Map.of(), false));

        operationSignatures.put("rename", renameSignature);

        var sortSignature = new ArrayList<AttributeValue>();
        sortSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));
        sortSignature.add(new AttributeValue(AttributeValue.Type.BOOLEAN, true, false));

        operationSignatures.put("sort", sortSignature);

        var countSignature = new ArrayList<AttributeValue>();
        countSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("count", countSignature);

        var maxSignature = new ArrayList<AttributeValue>();
        maxSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("max", maxSignature);

        var argMaxSignature = new ArrayList<AttributeValue>();
        argMaxSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("argMax", argMaxSignature);

        var minSignature = new ArrayList<AttributeValue>();
        minSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("min", minSignature);

        var argMinSignature = new ArrayList<AttributeValue>();
        argMinSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("argMin", argMinSignature);

        var sumSignature = new ArrayList<AttributeValue>();
        sumSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("sum", sumSignature);

        var meanSignature = new ArrayList<AttributeValue>();
        meanSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("mean", meanSignature);

        var stdSignature = new ArrayList<AttributeValue>();
        stdSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("std", stdSignature);

        var varSignature = new ArrayList<AttributeValue>();
        varSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("var", varSignature);

        var tableSignature = new ArrayList<AttributeValue>();
        tableSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", false));

        operationSignatures.put("table", tableSignature);

        var exportSignature = new ArrayList<AttributeValue>();
        exportSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("export", exportSignature);
    }

    static {
        exportSignatures = new HashMap<>();

        var csvAttributes = new HashMap<String, AttributeValue>();

        csvAttributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        csvAttributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        csvAttributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));
        csvAttributes.put("separator", new AttributeValue(AttributeValue.Type.STRING, CsvExporter.DEFAULT_SEPARATOR, false));

        exportSignatures.put("csv", csvAttributes);

        var htmlAttributes = new HashMap<String, AttributeValue>();

        htmlAttributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        htmlAttributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        htmlAttributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));
        htmlAttributes.put("title", new AttributeValue(AttributeValue.Type.STRING, HtmlExporter.DEFAULT_TITLE, false));
        htmlAttributes.put("style", new AttributeValue(AttributeValue.Type.STRING, HtmlExporter.DEFAULT_STYLE, false));
        htmlAttributes.put("exportFullHtml", new AttributeValue(AttributeValue.Type.BOOLEAN, HtmlExporter.DEFAULT_EXPORT_FULL_HTML, false));

        exportSignatures.put("html", htmlAttributes);

        var latexAttributes = new HashMap<String, AttributeValue>();

        latexAttributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        latexAttributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        latexAttributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));

        exportSignatures.put("latex", latexAttributes);

        var markdownAttributes = new HashMap<String, AttributeValue>();

        markdownAttributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        markdownAttributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        markdownAttributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));

        exportSignatures.put("markdown", markdownAttributes);

        var tsvAttributes = new HashMap<String, AttributeValue>();

        tsvAttributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        tsvAttributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        tsvAttributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));

        exportSignatures.put("tsv", tsvAttributes);
    }
}
