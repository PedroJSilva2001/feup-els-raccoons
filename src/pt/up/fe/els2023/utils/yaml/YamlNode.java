package pt.up.fe.els2023.utils.yaml;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public class YamlNode {
    private final JsonNode node;

    protected YamlNode(JsonNode node) {
        this.node = node;
    }

    public boolean has(String property) {
        return get(property) != null;
    }

    public boolean has(int index) {
        return get(index) != null;
    }

    public YamlNode get(String property) {
        var obj = node.isObject();
        var value = node.get(property);


        if (value == null) {
            return null;
        }

        return new YamlNode(value);
    }

    public YamlNode get(int index) {
        var value = node.get(index);

        if (value == null) {
            return null;
        }

        return new YamlNode(value);
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


/*package pt.up.fe.els2023.utils.yaml;

import java.util.List;
import java.util.Map;

public class YamlNode {
    private final Object node;

    protected YamlNode(Object node) {
        this.node = node;
    }

    public boolean has(String property) {
        return get(property) != null;
    }

    public boolean has(int index) {
        return get(index) != null;
    }

    public YamlNode get(String property) {
        if (!isObject()) {
            return null;
        }

        if (!((Map<String, Object>) node).containsKey(property)) {
            return null;
        }

        return new YamlNode(((Map<String, Object>) node).get(property));
    }

    public YamlNode get(int index) {
        if (!isArray()) {
            return null;
        }

        if (index < 0 || index >= ((List<Object>) node).size()) {
            return null;
        }

        return new YamlNode(((List<Object>) node).get(index));
    }

    public boolean isNull() {
        return node == null;
    }

    public boolean isBoolean() {
        if (isNull()) {
            return false;
        }

        return node instanceof Boolean;
    }

    public boolean isInteger() {
        if (isNull()) {
            return false;
        }

        return node instanceof Integer;
    }

    public boolean isDouble() {
        if (isNull()) {
            return false;
        }

        return node instanceof Double;
    }

    public boolean isText() {
        if (isNull()) {
            return false;
        }

        return node instanceof String;
    }

    public boolean isObject() {
        if (isNull()) {
            return false;
        }

        return node instanceof Map;
    }

    public boolean isArray() {
        if (isNull()) {
            return false;
        }

        return node instanceof List;
    }

    public boolean isContainer() {
        return isObject() || isArray();
    }

    public Boolean asBoolean() {
        if (isBoolean()) {
            return (Boolean) node;
        }

        if (isText()) {
            var text = (String) node;

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
        if (isInteger()) {
            return (Integer) node;
        }

        if (isText()) {
            try {
                return Integer.parseInt((String) node);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    public Double asDouble() {
        if (isDouble()) {
            return (Double) node;
        }

        if (isText()) {
            try {
                return Double.parseDouble((String) node);
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

        if (!isText()) {
            return node.toString();
        }

        return (String) node;
    }
}*/
