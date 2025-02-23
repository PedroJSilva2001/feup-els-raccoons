package pt.up.fe.els2023.utils.resources.json;

import pt.up.fe.els2023.utils.resources.ResourceNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class JsonNode implements ResourceNode {
    private final com.fasterxml.jackson.databind.JsonNode node;

    private final String name;

    protected JsonNode(com.fasterxml.jackson.databind.JsonNode node) {
        this.node = node;
        this.name = null;
    }

    protected JsonNode(com.fasterxml.jackson.databind.JsonNode node, String name) {
        this.node = node;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean has(String property) {
        return node.has(property);
    }

    @Override
    public boolean has(int index) {
        return node.has(index);
    }

    @Override
    public JsonNode get(String property) {
        var value = node.get(property);

        if (value == null) {
            return null;
        }

        return new JsonNode(value, property);
    }

    @Override
    public JsonNode get(int index) {
        var value = node.get(index);

        if (value == null) {
            return null;
        }

        return new JsonNode(value, name);
    }

    @Override
    public Iterator<ResourceNode> iterator() {
        Collection<ResourceNode> collection = new ArrayList<>();

        for (Iterator<com.fasterxml.jackson.databind.JsonNode> it = node.elements(); it.hasNext(); ) {
            collection.add(new JsonNode(it.next(), name));
        }

        return collection.iterator();
    }

    @Override
    public List<ResourceNode> getChildren() {
        if (isArray()) {
            return null;
        } else if (isValue()) {
            return null;
        }

        var children = new ArrayList<ResourceNode>();

        for (Iterator<Map.Entry<String, com.fasterxml.jackson.databind.JsonNode>> it = node.fields(); it.hasNext(); ) {
            var field = it.next();
            children.add(new JsonNode(field.getValue(), field.getKey()));
        }

        return children;
    }

    public String getNested(String propertyPath) {
        String[] properties = propertyPath.split("\\.");

        var currentNode = new JsonNode(node, name);

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


    @Override
    public boolean isNull() {
        return node.isNull();
    }

    @Override
    public boolean isBoolean() {
        return node.isBoolean();
    }

    @Override
    public boolean isInteger() {
        return node.isInt();
    }

    @Override
    public boolean isDouble() {
        return node.isFloat() || node.isDouble();
    }

    @Override
    public boolean isText() {
        return node.isTextual();
    }

    @Override
    public boolean isLong() {
        return node.isInt() || node.isLong();
    }

    @Override
    public boolean isBigDecimal() {
        return node.isBigDecimal();
    }

    @Override
    public boolean isBigInteger() {
        return node.isBigInteger();
    }

    @Override
    public boolean isObject() {
        return node.isObject();
    }

    @Override
    public boolean isArray() {
        return node.isArray();
    }

    @Override
    public boolean isValue() {
        return node.isValueNode();
    }

    @Override
    public boolean isContainer() {
        return node.isContainerNode();
    }

    @Override
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

    @Override
    public Long asLong() {
        if (isInteger() || isBoolean() || isDouble() || isLong()) {
            return node.asLong();
        }

        if (isText()) {
            try {
                return Long.parseLong(node.asText());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public BigDecimal asBigDecimal() {
        if (isInteger() || isBoolean() || isDouble() || isLong() || isBigInteger() || isBigDecimal()) {
            return node.decimalValue();
        }

        if (isText()) {
            try {
                return new BigDecimal(node.asText());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public BigInteger asBigInteger() {
        if (isInteger() || isBoolean() || isDouble() || isLong() || isBigInteger()) {
            return node.bigIntegerValue();
        }

        if (isText()) {
            try {
                return new BigInteger(node.asText());
            } catch (NumberFormatException e) {
                return null;
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

    @Override
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

    @Override
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
