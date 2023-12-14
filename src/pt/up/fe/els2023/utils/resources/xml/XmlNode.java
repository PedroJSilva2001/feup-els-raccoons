package pt.up.fe.els2023.utils.resources.xml;

import org.w3c.dom.Node;
import pt.up.fe.els2023.utils.resources.ResourceNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import pt.up.fe.els2023.utils.resources.json.JsonNode;

public class XmlNode implements ResourceNode {

    private final Node node;
    private final String name;

    protected XmlNode(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    private Node getValueOfNode() {
        if (node.getNodeType() == Element.ATTRIBUTE_NODE) {
            return node;
        }

        if (node.getNodeType() == Element.TEXT_NODE) {
            return node;
        }

        if (node.getNodeType() == Element.ELEMENT_NODE && node.getChildNodes().getLength() == 1
                && node.getFirstChild().getNodeType() == Element.TEXT_NODE) {
            return node.getFirstChild();
        }

        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean has(String property) {
        return get(property) != null;
    }

    @Override
    public boolean has(int index) {
        return get(index) != null;
    }

    @Override
    public boolean isArray() {
        return node.getNodeType() == Element.ELEMENT_NODE &&
                (node.getChildNodes().getLength() >= 1 &&
                        node.getFirstChild().getNodeType() == Element.ELEMENT_NODE) ||
                node.getChildNodes().getLength() > 1;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        Node value = getValueOfNode();
        if (value != null && value.getNodeValue() != null) {
            return value.getNodeValue().equals("true") || value.getNodeValue().equals("false");
        }

        return false;
    }

    @Override
    public boolean isInteger() {
        Node value = getValueOfNode();
        if (value != null) {
            try {
                Integer.parseInt(value.getNodeValue());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean isDouble() {
        Node value = getValueOfNode();
        if (value != null) {
            try {
                Double.parseDouble(value.getNodeValue());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean isText() {
        return isValue();
    }

    @Override
    public boolean isLong() {
        Node value = getValueOfNode();
        if (value != null) {
            try {
                Long.parseLong(value.getNodeValue());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean isBigDecimal() {
        // TODO
        return false;
    }

    @Override
    public boolean isBigInteger() {
        // TODO
        return false;
    }

    @Override
    public boolean isObject() {
        return node.getNodeType() == Element.ELEMENT_NODE &&
                (node.getChildNodes().getLength() >= 1 &&
                        node.getFirstChild().getNodeType() == Element.ELEMENT_NODE) ||
                node.getChildNodes().getLength() > 1;
    }

    @Override
    public boolean isValue() {
        return getValueOfNode() != null;
    }

    @Override
    public boolean isContainer() {
        return node.getNodeType() == Element.ELEMENT_NODE &&
                (node.getChildNodes().getLength() >= 1 &&
                node.getFirstChild().getNodeType() == Element.ELEMENT_NODE) ||
                node.getChildNodes().getLength() > 1;
    }

    @Override
    public ResourceNode get(String property) {
        var children = node.getChildNodes();


        for (var e = children.item(0); e != null; e = e.getNextSibling()) {
            if (e.getNodeType() == Element.ELEMENT_NODE) {
                if (e.getNodeName().equals(property)) {
                    return new XmlNode(e, property);
                }
            }
        }

        return null;
    }

    @Override
    public ResourceNode get(int index) {
        var children = node.getChildNodes();

        if (index < 0 || index >= children.getLength()) {
            return null;
        }

        var currentIdx = 0;

        for (var e = children.item(0); e != null; e = e.getNextSibling()) {
            if (e.getNodeType() == Element.ELEMENT_NODE) {
                if (currentIdx == index) {
                    return new XmlNode(e, e.getNodeName());
                }

                currentIdx++;
            }
        }

        return null;
    }

    @Override
    public Iterator<ResourceNode> iterator() {
        return getChildren().iterator();
    }

    @Override
    public List<ResourceNode> getChildren() {
        var children = new ArrayList<ResourceNode>();

        for (var e = node.getFirstChild(); e != null; e = e.getNextSibling()) {
            if (e.getNodeType() == Element.ELEMENT_NODE) {
                children.add(new XmlNode(e, e.getNodeName()));
            }
        }

        return children;
    }

    @Override
    public String getNested(String propertyPath) {
        return null;
    }

    @Override
    public Boolean asBoolean() {
        Node value = getValueOfNode();
        if (value == null) {
            return null;
        }

        if (isBoolean()) {
            return Boolean.parseBoolean(value.getNodeValue());
        }

        if (isInteger() || isLong()) {
            return Long.parseLong(value.getNodeValue()) != 0;
        }

        if (isDouble()) {
            return Double.parseDouble(value.getNodeValue()) != 0;
        }

        return null;
    }

    @Override
    public Long asLong() {
        Node value = getValueOfNode();
        if (value == null) {
            return null;
        }

        if (isInteger() || isLong()) {
            return Long.parseLong(value.getNodeValue());
        }

        if (isDouble()) {
            return Math.round(Double.parseDouble(value.getNodeValue()));
        }

        if (isBoolean()) {
            return Boolean.parseBoolean(value.getNodeValue()) ? 1L : 0L;
        }

        return null;
    }

    @Override
    public BigDecimal asBigDecimal() {
        // TODO
        return null;
    }

    @Override
    public BigInteger asBigInteger() {
        // TODO
        return null;
    }

    @Override
    public Double asDouble() {
        Node value = getValueOfNode();
        if (value == null) {
            return null;
        }

        if (isInteger() || isLong() || isDouble()) {
            return Double.parseDouble(value.getNodeValue());
        }

        if (isBoolean()) {
            return Boolean.parseBoolean(value.getNodeValue()) ? 1.0 : 0.0;
        }

        return null;
    }

    @Override
    public String asText() {
        Node value = getValueOfNode();
        if (value == null) {
            return node.getNodeValue();
        }

        return value.getNodeValue();
    }
}
