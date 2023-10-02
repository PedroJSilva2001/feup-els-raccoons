package pt.up.fe.els2023.utils.resources.yaml;

import pt.up.fe.els2023.utils.resources.json.JsonNode;

public class YamlNode extends JsonNode {
    protected YamlNode(com.fasterxml.jackson.databind.JsonNode node) {
        super(node);
    }
}