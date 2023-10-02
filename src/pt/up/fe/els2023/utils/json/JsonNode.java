package pt.up.fe.els2023.utils.json;

import pt.up.fe.els2023.utils.yaml.YamlNode;

public class JsonNode {
    private final com.fasterxml.jackson.databind.JsonNode node;

    protected JsonNode(com.fasterxml.jackson.databind.JsonNode node) {
        this.node = node;
    }

    public boolean has(String property) {
        return node.has(property);
    }

    public boolean has(int index) {
        return node.has(index);
    }

    public JsonNode get(String property) {
        var value = node.get(property);

        if (value == null) {
            return null;
        }

        return new JsonNode(value);
    }

    public JsonNode get(int index) {
        var value = node.get(index);

        if (value == null) {
            return null;
        }

        return new JsonNode(value);
    }

    public boolean isNull() {
        return node.isNull();
    }

    public boolean isBoolean() {
        return node.isBoolean();
    }

    public boolean isInteger() {
        return node.isIntegralNumber();
    }

    public boolean isDouble() {
        return node.isDouble();
    }

    public boolean isText() {
        return node.isTextual();
    }

    public boolean isObject() {
        return node.isObject();
    }

    public boolean isArray() {
        return node.isArray();
    }

    public boolean isValue() {
        return node.isValueNode();
    }

    public boolean isContainer() {
        return node.isContainerNode();
    }

    public Boolean asBoolean() {
        if (isBoolean() || isInteger()) {
            return node.asBoolean();
        }

        if (isText()) {
            var text = node.asText();

            if ("true".equalsIgnoreCase(text)) {
                return Boolean.TRUE;
            }

            if ("false".equalsIgnoreCase(text)) {
                return Boolean.FALSE;
            }
        }

        return null;
    }

    public Integer asInteger() {
        if (isInteger() || isBoolean() || isDouble()) {
            return node.asInt();
        }

        if (isText()) {
            try {
                return Integer.parseInt(node.asText());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    public Double asDouble() {
        if (isDouble() || isInteger() || isBoolean()) {
            return node.asDouble();
        }

        if (isText()) {
            try {
                return Double.parseDouble(node.asText());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    public String asText() {
        if (isNull()) {
            return "";
        }
        if (isValue()) {
            return node.asText();
        }

        return node.toString();
    }
}
