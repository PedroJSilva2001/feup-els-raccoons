package pt.up.fe.els2023;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.export.*;
import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.sources.YamlSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<Map<String, Object>> tableSources = (ArrayList<Map<String, Object>>) yamlData.get("sources");

        for (Map<String, Object> tableSource : tableSources) {
            Object files = tableSource.get("path");
            String sourceName = (String) tableSource.get("name");
            List<String> filesList = files instanceof String ? new ArrayList<>(List.of((String) files)) : (ArrayList<String>) files;
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

    private String unescapeString(String string) {
        // This unescapes the $ at the beginning of the keyName
        Pattern escapePattern = Pattern.compile("^\\\\+\\$");
        Matcher escapeMatcher = escapePattern.matcher(string);
        if (escapeMatcher.find()) {
            return string.replaceFirst("\\\\", "");
        }

        return string;
    }

    private SchemaNode parseValue(Object value) {
        if (value instanceof List) {
            List<Object> eachListNode = (List<Object>) value;
            return parseListNode(eachListNode);
        } else if (value instanceof Map) {
            // This could cause ambiguity, for example
            // nft:
            //    - params:
            //        min_split: min_split_value
            // is min_split_value the name of the column, or is it a key in the min_split map?
            // the only correct way is for min_split_value to be the name of the column, but this
            // removes the ability of not having to specify the name of the column, and using the key as default
            //
            // TODO: for now, we will allow this, but we should probably change this in the future
            Map<String, Object> eachMapNode = (Map<String, Object>) value;
            return parseMapNode(eachMapNode);
        } else if (value instanceof String columnName) {
            return new ColumnNode(columnName);
        } else {
            return new NullNode();
        }
    }

    private SchemaNode parseMapNode(Map<String, Object> mapNode) {
        // Should only have one key
        String keyName = mapNode.keySet().iterator().next();
        Object value = mapNode.get(keyName);

        if (Objects.equals(keyName, "$each")) {
            return new EachNode(parseValue(value));
        } else if (Objects.equals(keyName, "$except")) {
            List<String> exceptList = new ArrayList<>();

            if (value instanceof String) {
                exceptList.add((String) value);
            } else if (value instanceof List) {
                List<Object> exceptListNode = (List<Object>) value;

                for (Object except : exceptListNode) {
                    if (except instanceof String) {
                        exceptList.add((String) except);
                    }
                }
            }

            return new ExceptNode(new HashSet<>(exceptList));
        } else if (Objects.equals(keyName, "$file")) {
            String columnName = (String) value;
            return new FileNode(columnName);
        } else if (Objects.equals(keyName, "$directory")) {
            String columnName = (String) value;
            return new DirectoryNode(columnName);
        } else if (Objects.equals(keyName, "$path")) {
            String columnName = (String) value;
            return new PathNode(columnName);
        }

        Pattern pattern = Pattern.compile("^\\$(.*)\\[(\\d+)]");
        Matcher matcher = pattern.matcher(keyName);
        if (matcher.matches()) {
            String childName = matcher.group(1);
            int index = Integer.parseInt(matcher.group(2));

            if (childName == null || Objects.equals(childName, "")) {
                return new IndexNode(index, parseValue(value));
            }

            return new IndexOfNode(index, childName, parseValue(value));
        }

        keyName = unescapeString(keyName);
        return new PropertyNode(keyName, parseValue(value));
    }

    private ListNode parseListNode(List<Object> listNode) {
        List<SchemaNode> schemaNodes = new ArrayList<>();

        for (Object node : listNode) {
            if (node instanceof String keyName) {
                switch (keyName) {
                    case "$all" -> {
                        schemaNodes.add(new AllNode());
                        continue;
                    }
                    case "$each" -> {
                        schemaNodes.add(new EachNode(new NullNode()));
                        continue;
                    }
                    case "$file" -> {
                        schemaNodes.add(new FileNode());
                        continue;
                    }
                    case "$path" -> {
                        schemaNodes.add(new PathNode());
                        continue;
                    }
                    case "$directory" -> {
                        schemaNodes.add(new DirectoryNode());
                        continue;
                    }
                    case "$all-value" -> {
                        schemaNodes.add(new AllValueNode());
                        continue;
                    }
                    case "$all-container" -> {
                        schemaNodes.add(new AllContainerNode());
                        continue;
                    }
                }

                keyName = unescapeString(keyName);
                schemaNodes.add(new PropertyNode(keyName, new NullNode()));
            } else if (node instanceof Map) {
                Map<String, Object> mapNode = (Map<String, Object>) node;
                schemaNodes.add(parseMapNode(mapNode));
            } else {
                schemaNodes.add(new NullNode());
            }
        }

        return new ListNode(schemaNodes);
    }

    private List<TableSchema> parseTableSchemas(Map<String, Object> yamlData, Map<String, TableSource> configTableSources) {
        if (!yamlData.containsKey("tables")) {
            System.out.println("No tableSchemas found");
            return null;
        }

        List<TableSchema> configTableSchemas = new ArrayList<>();
        List<Map<String, Object>> tableSchemas = (ArrayList<Map<String, Object>>) yamlData.get("tables");

        for (Map<String, Object> tableSchema : tableSchemas) {
            String name = (String) tableSchema.get("name");
            TableSource source = configTableSources.get((String) tableSchema.get("source"));
            TableSchema table = new TableSchema(name);

            if (source != null) {
                table.source(source);
            } else {
                System.out.println("Source not found");
                // TODO: SPECIFY LINE AND THROW EXCEPTION
            }

            List<Object> nft = (ArrayList<Object>) tableSchema.get("nft");

            if (nft == null) {
                System.out.println("No nft found");
                // TODO: SPECIFY LINE AND THROW EXCEPTION
                throw new RuntimeException();
            }

            ListNode nftNode = parseListNode(nft);
            table.nft(nftNode.list());

            configTableSchemas.add(table);
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
                    continue;
                }
            }

            // TODO: use enum for endOfLine, for example CR, LF, CRLF
            if (export.containsKey("endOfLine")) {
                configExporterBuilder.setEndOfLine((String) export.get("endOfLine"));
            }
            configExporter.add(configExporterBuilder.build());
        }

        return configExporter;
    }
}
