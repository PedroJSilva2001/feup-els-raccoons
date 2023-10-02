package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.yaml.YamlNode;
import pt.up.fe.els2023.utils.yaml.YamlParser;

import java.io.*;
import java.util.List;

public class YamlSource extends TableSource<YamlNode> {
    public YamlSource(String name, List<String> files) {
        super(name, files);
    }

    @Override
    protected YamlNode getResourceRootNode(Reader reader) throws IOException {
        return YamlParser.parse(reader);
    }

    protected String getPropertyValue(YamlNode rootNode, String propertyPath) {
        String[] properties = propertyPath.split("\\.");

        YamlNode currentNode = rootNode;

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
            return "n";
        }

        return currentNode.asText();
    }
}
