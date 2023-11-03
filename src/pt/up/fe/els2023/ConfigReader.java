package pt.up.fe.els2023;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import pt.up.fe.els2023.config.Config;
import pt.up.fe.els2023.config.TableSchema;
import pt.up.fe.els2023.export.*;
import pt.up.fe.els2023.operations.*;
import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.sources.YamlSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@SuppressWarnings(value = "unchecked")
public class ConfigReader {
    private final ObjectMapper objectMapper;
    private final String configFile;

    public ConfigReader(String configFile) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.configFile = configFile;
    }

    Config readConfig() throws IOException {
        File file = new File(configFile);
        Map<String, Object> yamlData;
        try (YAMLParser parser = new YAMLFactory().createParser(file)) {
            yamlData = objectMapper.readValue(parser, new TypeReference<>() {
            });
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found");
            return null;
        }

        Map<String, TableSource> configTableSources = parseTableSources(yamlData);
        List<TableSchema> configTableSchemas = parseTableSchemas(yamlData, configTableSources);
        List<Pipeline> configOperations = parseOperations(yamlData);
        List<TableExporter> configExporter = parseExporters(yamlData);

        return new Config(configTableSources, configTableSchemas, configOperations, configExporter);
    }

    private List<Pipeline> parseOperations(Map<String, Object> yamlData) {
        if (!yamlData.containsKey("operations")) {
            System.out.println("No operations found");
            return null;
        }

        List<Pipeline> configOperations = new ArrayList<>();
        List<Map<String, Object>> operations = (ArrayList<Map<String, Object>>) yamlData.get("operations");

        if (operations == null) {
            System.out.println("No operations found");
            return null;
        }

        for (Map<String, Object> operation : operations) {
            String initialTable = null;
            String result = null;
            List<TableOperation> ops = new ArrayList<>();
            if (operation.containsKey("pipeline")) {
                Map<String, Object> pipeline = (Map<String, Object>) operation.get("pipeline");
                initialTable = (String) pipeline.get("table");
                result = (String) pipeline.get("result");
                for (Map<String, Object> pipelineOperation : (ArrayList<Map<String, Object>>) pipeline.get("operations")) {
                    TableOperation op = parseOperationNode(pipelineOperation);
                    if (op != null) {
                        ops.add(op);
                    }
                }
            } else if (operation.containsKey("operation")) {
                TableOperation op = parseOperationNode(operation);
                if (op != null) {
                    initialTable = (String) operation.get("table");
                    result = (String) operation.get("result");
                    ops.add(op);
                }
            } else {
                System.out.println("No operation found");
            }

            if (!ops.isEmpty() && initialTable != null && result != null) {
                Pipeline pipeline = new Pipeline(initialTable, result, ops);
                configOperations.add(pipeline);
            }
        }

        return configOperations;
    }

    private TableOperation parseOperationNode(Map<String, Object> operationNode) {
        switch ((String) operationNode.get("operation")) {
            case "argMax" -> {
                return new ArgMaxOperation((String) operationNode.get("columns"));
            }
            case "argMin" -> {
                return new ArgMinOperation((String) operationNode.get("columns"));
            }
            case "concat" -> {
                Object additionalTablesObject = operationNode.get("additionalTables");
                List<String> additionalTables = additionalTablesObject instanceof String ? new ArrayList<>(List.of((String) additionalTablesObject)) : (ArrayList<String>) additionalTablesObject;
                if (Objects.equals(operationNode.get("axis"), "horizontal")) {
                    return new ConcatHorizontalOperation(additionalTables);
                } else if (Objects.equals(operationNode.get("axis"), "vertical")) {
                    System.out.println("Unsupported operation");
                }
            }
            case "select" -> {
                Object columnsObject = operationNode.get("columns");
                List<String> columns = columnsObject instanceof String ? new ArrayList<>(List.of((String) columnsObject)) : (ArrayList<String>) columnsObject;
                return new SelectOperation(columns);
            }
            case "reject" -> {
                Object columnsObject = operationNode.get("columns");
                List<String> columns = columnsObject instanceof String ? new ArrayList<>(List.of((String) columnsObject)) : (ArrayList<String>) columnsObject;
                return new RejectOperation(columns);
            }
            case "export" -> {
                operationNode.put("name", operationNode.get("table"));
                operationNode.put("filename", operationNode.get("result"));
                var exporterBuilder = parseExportNode(operationNode);
                if (exporterBuilder != null) {
                    return new ExportOperation(exporterBuilder.build());
                }
            }
            case "where" -> {
                return new WhereOperation((String) operationNode.get("condition"));
            }
            default -> System.out.println("Unsupported operation");
        }
        return null;
    }

    private Map<String, TableSource> parseTableSources(Map<String, Object> yamlData) {
        if (!yamlData.containsKey("sources")) {
            System.out.println("No tableSources found");
            return null;
        }

        Map<String, TableSource> configTableSources = new HashMap<>();
        ArrayList<Map<String, Object>> tableSources = (ArrayList<Map<String, Object>>) yamlData.get("sources");

        for (Map<String, Object> tableSource : tableSources) {
            Object files = tableSource.get("path");
            String sourceName = (String) tableSource.get("name");
            ArrayList<String> filesList = files instanceof String ? new ArrayList<>(List.of((String) files)) : (ArrayList<String>) files;
            switch ((String) tableSource.get("type")) {
                // TODO
                case "json" -> configTableSources.put(sourceName, new JsonSource(sourceName, filesList));
                case "yaml" -> configTableSources.put(sourceName, new YamlSource(sourceName, filesList));
                case "xml" -> System.out.println("TODO: xmlSource");
                case "csv" -> System.out.println("TODO: csvSource");
                default -> System.out.println("Unsupported source type");
            }
        }

        return configTableSources;
    }

    private List<TableSchema> parseTableSchemas(Map<String, Object> yamlData, Map<String, TableSource> configTableSources) {
        if (!yamlData.containsKey("tables")) {
            System.out.println("No tableSchemas found");
            return null;
        }

        List<TableSchema> configTableSchemas = new ArrayList<>();
        ArrayList<Map<String, Object>> tableSchemas = (ArrayList<Map<String, Object>>) yamlData.get("tables");

        for (Map<String, Object> tableSchema : tableSchemas) {
            String name = (String) tableSchema.get("name");
            TableSource source = configTableSources.get((String) tableSchema.get("source"));
            ArrayList<Map<String, Object>> columns = (ArrayList<Map<String, Object>>) tableSchema.get("columns");
            for (Map<String, Object> column : columns) {
                String columnFrom = source != null ? (String) column.get("from") : null;
                String columnName = column.get("name") != null ? (String) column.get("name") : columnFrom;
            }
            configTableSchemas.add(new TableSchema(name));
        }

        return configTableSchemas;
    }

    private List<TableExporter> parseExporters(Map<String, Object> yamlData) {
        if (!yamlData.containsKey("export")) {
            System.out.println("No exporters found");
            return null;
        }

        List<TableExporter> configExporter = new ArrayList<>();
        ArrayList<Map<String, Object>> exporters = (ArrayList<Map<String, Object>>) yamlData.get("export");

        for (Map<String, Object> export : exporters) {
            TableExporterBuilder<?> configExporterBuilder = parseExportNode(export);
            if (configExporterBuilder == null) {
                continue;
            }

            configExporter.add(configExporterBuilder.build());
        }

        return configExporter;
    }

    private TableExporterBuilder<?> parseExportNode(Map<String, Object> export) {
        TableExporterBuilder<?> configExporterBuilder;
        // TODO: VALIDATE
        switch ((String) export.get("format")) {
            case "csv" -> {
                CsvExporterBuilder csvExporterBuilder = new CsvExporterBuilder((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"));
                if (export.containsKey("separator")) {
                    csvExporterBuilder.setSeparator((String) export.get("separator"));
                }

                configExporterBuilder = csvExporterBuilder;
            }
            case "tsv" ->
                    configExporterBuilder = new TsvExporterBuilder((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"));
            case "html" -> {
                HtmlExporterBuilder htmlExporterBuilder = new HtmlExporterBuilder((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"));
                if (export.containsKey("title")) {
                    htmlExporterBuilder.setTitle((String) export.get("title"));
                }
                if (export.containsKey("style")) {
                    htmlExporterBuilder.setStyle((String) export.get("style"));
                }
                if (export.containsKey("exportFullHtml")) {
                    htmlExporterBuilder.setExportFullHtml((boolean) export.get("exportFullHtml"));
                }

                configExporterBuilder = htmlExporterBuilder;
            }
            case "latex" ->
                    configExporterBuilder = new LatexExporterBuilder((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"));
            case "markdown", "md" ->
                    configExporterBuilder = new MarkdownExporterBuilder((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"));
            default -> {
                //TODO: SPECIFY FORMAT AND LINE
                System.out.println("Unsupported format");
                return null;
            }
        }

        // TODO: use enum for endOfLine, for example CR, LF, CRLF
        if (export.containsKey("endOfLine")) {
            configExporterBuilder.setEndOfLine((String) export.get("endOfLine"));
        }

        return configExporterBuilder;
    }
}
