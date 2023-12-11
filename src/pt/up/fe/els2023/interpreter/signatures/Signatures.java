package pt.up.fe.els2023.interpreter.signatures;

import pt.up.fe.els2023.export.CsvExporter;
import pt.up.fe.els2023.export.HtmlExporter;
import pt.up.fe.els2023.model.operations.*;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.racoons.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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
            TABLE_SCHEMA,
            EXPORTER
        }
    }


    public static final Map<String, List<AttributeValue>> operationSignatures;
    public static final Map<String, Class<?>> operationClass;
    public static final Map<String, Map<String, AttributeValue>> exportSignatures;

    static {
        operationSignatures = new HashMap<>();
        operationClass = new HashMap<>();

        var joinSignature = new ArrayList<AttributeValue>();
        joinSignature.add(new AttributeValue(AttributeValue.Type.TABLE, null, true));
        joinSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("join", joinSignature);
        operationClass.put("join", JoinOperation.class);

        var selectSignature = new ArrayList<AttributeValue>();
        selectSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("select", selectSignature);
        operationClass.put("select", SelectOperation.class);

        var rejectSignature = new ArrayList<AttributeValue>();
        rejectSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("reject", rejectSignature);
        operationClass.put("reject", RejectOperation.class);

        var whereSignature = new ArrayList<AttributeValue>();
        whereSignature.add(new AttributeValue(AttributeValue.Type.EXPRESSION, null, true));

        operationSignatures.put("where", whereSignature);
        operationClass.put("where", WhereOperation.class);

        var dropWhereSignature = new ArrayList<AttributeValue>();
        dropWhereSignature.add(new AttributeValue(AttributeValue.Type.EXPRESSION, null, true));

        operationSignatures.put("dropWhere", dropWhereSignature);
        operationClass.put("dropWhere", DropWhereOperation.class);

        var concatVerticalSignature = new ArrayList<AttributeValue>();
        concatVerticalSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_TABLE, List.of(), false));

        operationSignatures.put("concatVertical", concatVerticalSignature);
        operationClass.put("concatVertical", ConcatVerticalOperation.class);

        var concatHorizontalSignature = new ArrayList<AttributeValue>();
        concatHorizontalSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_TABLE, List.of(), false));

        operationSignatures.put("concatHorizontal", concatHorizontalSignature);
        operationClass.put("concatHorizontal", ConcatHorizontalOperation.class);

        var renameSignature = new ArrayList<AttributeValue>();
        renameSignature.add(new AttributeValue(AttributeValue.Type.STRING_MAP, Map.of(), false));

        operationSignatures.put("rename", renameSignature);
        operationClass.put("rename", RenameOperation.class);

        var sortSignature = new ArrayList<AttributeValue>();
        sortSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));
        sortSignature.add(new AttributeValue(AttributeValue.Type.BOOLEAN, true, false));

        operationSignatures.put("sort", sortSignature);
        operationClass.put("sort", SortOperation.class);

        var countSignature = new ArrayList<AttributeValue>();
        countSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("count", countSignature);
        operationClass.put("count", CountOperation.class);

        var maxSignature = new ArrayList<AttributeValue>();
        maxSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("max", maxSignature);
        operationClass.put("max", MaxOperation.class);

        var argMaxSignature = new ArrayList<AttributeValue>();
        argMaxSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("argMax", argMaxSignature);
        operationClass.put("argMax", ArgMaxOperation.class);

        var minSignature = new ArrayList<AttributeValue>();
        minSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("min", minSignature);
        operationClass.put("min", MinOperation.class);

        var argMinSignature = new ArrayList<AttributeValue>();
        argMinSignature.add(new AttributeValue(AttributeValue.Type.STRING, "", true));

        operationSignatures.put("argMin", argMinSignature);
        operationClass.put("argMin", ArgMinOperation.class);

        var sumSignature = new ArrayList<AttributeValue>();
        sumSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("sum", sumSignature);
        operationClass.put("sum", SumOperation.class);

        var meanSignature = new ArrayList<AttributeValue>();
        meanSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("mean", meanSignature);
        operationClass.put("mean", MeanOperation.class);

        var stdSignature = new ArrayList<AttributeValue>();
        stdSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("std", stdSignature);
        operationClass.put("std", StdOperation.class);

        var varSignature = new ArrayList<AttributeValue>();
        varSignature.add(new AttributeValue(AttributeValue.Type.VARIADIC_STRING, List.of(), false));

        operationSignatures.put("var", varSignature);
        operationClass.put("var", VarOperation.class);

        var tableSignature = new ArrayList<AttributeValue>();
        tableSignature.add(new AttributeValue(AttributeValue.Type.TABLE_SCHEMA, "", false));

        operationSignatures.put("table", tableSignature);
        operationClass.put("table", TableCreateOperation.class);

        var exportSignature = new ArrayList<AttributeValue>();
        exportSignature.add(new AttributeValue(AttributeValue.Type.EXPORTER, "", true));

        operationSignatures.put("export", exportSignature);
        operationClass.put("export", ExportOperation.class);
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

    public static TableOperation createOperation(String name, List<Object> parameters) {
        var signature = operationSignatures.get(name);

        if (signature == null) {
            throw new RuntimeException("Operation " + name + " not found");
        }

        var operationClass = Signatures.operationClass.get(name);

        if (operationClass == null) {
            throw new RuntimeException("Operation " + name + " not found");
        }

        var constructor = operationClass.getConstructors()[0];

        var parameterTypes = constructor.getParameterTypes();

        var parameterValues = new Object[parameterTypes.length];
        var currentParameterIndex = 0;

        for (int i = 0; i < parameterTypes.length; i++) {
            var parameterType = parameterTypes[i];
            var parameter = parameters.size() > currentParameterIndex ? parameters.get(currentParameterIndex) : null;
            var parameterSignature = signature.get(i);

            if (parameterType == String.class && parameterSignature.type() == AttributeValue.Type.STRING && parameter instanceof String) {
                parameterValues[i] = parameter;
            } else if (parameterType == List.class && parameter instanceof List) {
                if (parameterSignature.type() == AttributeValue.Type.VARIADIC_STRING || parameterSignature.type() == AttributeValue.Type.VARIADIC_TABLE) {
                    parameterValues[i] = parameter;
                }
            } else if ((parameterType == Boolean.class || parameterType == boolean.class ) && parameterSignature.type() == AttributeValue.Type.BOOLEAN && parameter instanceof Boolean) {
                parameterValues[i] = parameter;
            } else if (parameterType == Predicate.class && parameterSignature.type() == AttributeValue.Type.EXPRESSION && parameter instanceof Predicate) {
                parameterValues[i] = parameter;
            } else if (parameterType == Table.class && parameterSignature.type() == AttributeValue.Type.TABLE && parameter instanceof Table) {
                parameterValues[i] = parameter;
            } else if (parameterType == Map.class && parameterSignature.type() == AttributeValue.Type.STRING_MAP && parameter instanceof Map) {
                parameterValues[i] = parameter;
            } else {
                if (parameterSignature.required()) {
                    throw new RuntimeException("Parameter " + i + " of operation " + name + " is required");
                } else {
                    parameterValues[i] = parameterSignature.defaultValue();
                    continue;
                }
            }

            currentParameterIndex++;
        }

        try {
            return (TableOperation) constructor.newInstance(parameterValues);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
