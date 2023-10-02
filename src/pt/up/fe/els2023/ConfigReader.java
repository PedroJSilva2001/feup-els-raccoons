package pt.up.fe.els2023;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import pt.up.fe.els2023.export.*;
import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.sources.YamlSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            yamlData = objectMapper.readValue(parser, new TypeReference<>(){});
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found");
            return null;
        }

        Map<String, TableSource> configTableSources = parseTableSources(yamlData);
        List<TableSchema> configTableSchemas = parseTableSchemas(yamlData, configTableSources);
        // TODO: operations
        List<TableExporter> configExporter = parseExporters(yamlData);

        return new Config(configTableSources, configTableSchemas, null, configExporter);
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
            ArrayList<ColumnSchema> configColumns = new ArrayList<>();
            for (Map<String, Object> column : columns) {
                String columnFrom = source != null ? (String) column.get("from") : null;
                String columnName = column.get("name") != null ? (String) column.get("name") : columnFrom;
                configColumns.add(new ColumnSchema(columnName, columnFrom));
            }
            configTableSchemas.add(new TableSchema(name, configColumns, source));
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
            switch ((String) export.get("format")) {
                case "csv" ->
                        configExporter.add(new CsvExporter((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"), (String) export.get("endOfLine"), (String) export.get("separator")));
                case "tsv" ->
                        configExporter.add(new TsvExporter((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"), (String) export.get("endOfLine")));
                case "html" ->
                        configExporter.add(new HtmlExporter((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"), (String) export.get("endOfLine"), (String) export.get("title"), (String) export.get("style"), (boolean) export.get("exportFullHtml")));
                case "latex" ->
                        configExporter.add(new LatexExporter((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"), (String) export.get("endOfLine")));
                case "markdown", "md" ->
                        configExporter.add(new MarkdownExporter((String) export.get("name"), (String) export.get("filename"), (String) export.get("path"), (String) export.get("endOfLine")));
                default -> System.out.println("Unsupported format");
            }
        }

        return configExporter;
    }
}
