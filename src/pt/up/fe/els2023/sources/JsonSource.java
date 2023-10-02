package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.json.JsonNode;
import pt.up.fe.els2023.utils.json.JsonParser;

import java.io.*;
import java.util.List;

public class JsonSource extends TableSource<JsonNode> {
    public JsonSource(String name, List<String> files) {
        super(name, files);
    }

    @Override
    protected JsonNode getResourceRootNode(Reader reader) throws IOException {
        return JsonParser.parse(reader);
    }

    protected String getPropertyValue(JsonNode rootNode, String propertyPath) {
        String[] properties = propertyPath.split("\\.");

        var currentNode = rootNode;

        for (var prop : properties) {
            if (currentNode.isArray()) {
                System.out.println("Property path not found: Array properties not supported");
                return null;
            }

            if (currentNode.has(prop)) {
                currentNode = currentNode.get(prop);
            } else {
                System.out.println("Property not found: " + prop);
                return null;
            }
        }

        if (currentNode.isNull()) {
            return "";
        }

        return currentNode.asText();
    }
}