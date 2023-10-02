package pt.up.fe.els2023.utils.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.utils.resources.json.JsonNode;
import pt.up.fe.els2023.utils.resources.yaml.YamlNode;
import pt.up.fe.els2023.utils.resources.yaml.YamlParser;

import java.io.IOException;
import java.io.StringReader;

public class YamlNodeTest {
    private YamlParser parser = new YamlParser();

    private YamlNode yamlNode(String s) throws IOException {
        return parser.parse(new StringReader(s));
    }

    @Test
    public void testIsNull() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertTrue(nullNode.isNull());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertFalse(booleanNode.isNull());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isNull());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isNull());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isNull());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isNull());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isNull());

    }

    @Test
    public void testIsBoolean() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isBoolean());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertTrue(booleanNode.isBoolean());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isBoolean());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isBoolean());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isBoolean());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isBoolean());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isBoolean());
    }

    @Test
    public void testIsInteger() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isInteger());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertFalse(booleanNode.isInteger());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertTrue(integerNode.isInteger());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isInteger());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isInteger());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isInteger());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isInteger());
    }

    @Test
    public void testIsDouble() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isDouble());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertFalse(booleanNode.isDouble());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isDouble());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertTrue(doubleNode.isDouble());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isDouble());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isDouble());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isDouble());
    }

    @Test
    public void testIsText() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isText());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertFalse(booleanNode.isText());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isText());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isText());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertTrue(stringNode.isText());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isText());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isText());
    }

    @Test
    public void testIsObject() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isObject());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertFalse(booleanNode.isObject());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isObject());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isObject());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isObject());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertTrue(objectNode.isObject());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isObject());
    }

    @Test
    public void testIsArray() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isArray());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertFalse(booleanNode.isArray());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isArray());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isArray());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isArray());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isArray());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertTrue(arrayNode.isArray());
    }

    @Test
    public void testIsValue() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertTrue(nullNode.isValue());

        YamlNode booleanNode1 = yamlNode("false");
        Assertions.assertTrue(booleanNode1.isValue());

        YamlNode booleanNode2 = yamlNode("true");
        Assertions.assertTrue(booleanNode2.isValue());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertTrue(integerNode.isValue());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertTrue(doubleNode.isValue());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertTrue(stringNode.isValue());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertFalse(objectNode.isValue());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertFalse(arrayNode.isValue());
    }

    @Test
    public void testIsContainer() throws IOException {
        YamlNode nullNode = yamlNode("null");
        Assertions.assertFalse(nullNode.isContainer());

        YamlNode booleanNode1 = yamlNode("false");
        Assertions.assertFalse(booleanNode1.isContainer());

        YamlNode booleanNode2 = yamlNode("true");
        Assertions.assertFalse(booleanNode2.isContainer());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertFalse(integerNode.isContainer());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertFalse(doubleNode.isContainer());

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isContainer());

        YamlNode objectNode = yamlNode("key: value");
        Assertions.assertTrue(objectNode.isContainer());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertTrue(arrayNode.isContainer());
    }
    @Test
    public void testGetStringProperty() throws IOException {
        YamlNode objectNode = yamlNode("name: John\nage: 30");
        Assertions.assertEquals("John", objectNode.get("name").asText());
        Assertions.assertEquals(30, objectNode.get("age").asInteger());
        Assertions.assertNull(objectNode.get("address"));

        YamlNode arrayNode = yamlNode("- item1\n- item2\n- item3");
        Assertions.assertNull(arrayNode.get("name"));
        Assertions.assertNull(arrayNode.get("age"));

        YamlNode nullNode = yamlNode("null");
        Assertions.assertNull(nullNode.get("name"));
        Assertions.assertNull(nullNode.get("age"));

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertNull(booleanNode.get("name"));
        Assertions.assertNull(booleanNode.get("age"));

        YamlNode integerNode = yamlNode("3");
        Assertions.assertNull(integerNode.get("name"));
        Assertions.assertNull(integerNode.get("age"));

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertNull(doubleNode.get("name"));
        Assertions.assertNull(doubleNode.get("age"));

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertNull(stringNode.get("name"));
        Assertions.assertNull(stringNode.get("age"));
    }

    @Test
    public void testGetIndexedItem() throws IOException {
        YamlNode arrayNode = yamlNode("- item1\n- item2\n- item3");
        Assertions.assertEquals("item1", arrayNode.get(0).asText());
        Assertions.assertEquals("item2", arrayNode.get(1).asText());
        Assertions.assertEquals("item3", arrayNode.get(2).asText());
        Assertions.assertNull(arrayNode.get(-1));
        Assertions.assertNull(arrayNode.get(3));

        YamlNode objectNode = yamlNode("name: John\nage: 30");
        Assertions.assertNull(objectNode.get(0));
        Assertions.assertNull(objectNode.get(-1));

        YamlNode nullNode = yamlNode("null");
        Assertions.assertNull(nullNode.get(0));
        Assertions.assertNull(nullNode.get(-1));

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertNull(booleanNode.get(0));
        Assertions.assertNull(booleanNode.get(-1));

        YamlNode integerNode = yamlNode("3");
        Assertions.assertNull(integerNode.get(0));
        Assertions.assertNull(integerNode.get(-1));

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertNull(doubleNode.get(0));
        Assertions.assertNull(doubleNode.get(-1));

        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertNull(stringNode.get(0));
        Assertions.assertNull(stringNode.get(-1));
    }

    @Test
    public void testGetNestedProperty() throws IOException {
        String yamlData = "prop1: 1\nprop2:\n   prop3: val3\n   prop4:\n   - 1\n   - 2\n   - 3\nprop5:";

        YamlNode rootNode = yamlNode(yamlData);


        // Test getting a valid property
        String result = rootNode.getNested("prop1");
        Assertions.assertEquals("1", result);

        // Test getting a non-existent property
        result = rootNode.getNested("nonexistent");
        Assertions.assertNull(result);

        // Test getting an object value
        result = rootNode.getNested("prop2");
        Assertions.assertEquals("{\"prop3\":\"val3\",\"prop4\":[1,2,3]}", result);

        // Test getting a null value
        result = rootNode.getNested("prop5");
        Assertions.assertEquals("", result);

        // Test getting a valid property with nesting
        result = rootNode.getNested("prop2.prop3");
        Assertions.assertEquals("val3", result);
    }

    @Test
    public void testGetNestedPropertyArrayUnsupported() throws IOException {
        String yamlData1 = "prop1:\n   - 1\n   - 2\n   - 3";

        YamlNode rootNode1 = yamlNode(yamlData1);

        String yamData2 = "- 1\n- 2\n- 3]";

        JsonNode rootNode2 = yamlNode(yamData2);

        // Test getting a property where an array is encountered (unsupported)
        String result = rootNode1.getNested("prop1.item1");
        Assertions.assertNull(result);

        result = rootNode2.getNested("item1");
        Assertions.assertNull(result);
    }


    @Test
    public void testAsBoolean() throws IOException {
        YamlNode booleanNode1 = yamlNode("false");
        Assertions.assertEquals(false, booleanNode1.asBoolean());

        YamlNode booleanNode2 = yamlNode("true");
        Assertions.assertEquals(true, booleanNode2.asBoolean());

        YamlNode nullNode = yamlNode("null");
        Assertions.assertNull(nullNode.asBoolean());

        YamlNode integerNode1 = yamlNode("3");
        Assertions.assertEquals(true, integerNode1.asBoolean());

        YamlNode integerNode2 = yamlNode("1");
        Assertions.assertEquals(true, integerNode2.asBoolean());

        YamlNode integerNode3 = yamlNode("0");
        Assertions.assertEquals(false, integerNode3.asBoolean());

        YamlNode doubleNode1 = yamlNode("22.1");
        Assertions.assertNull(doubleNode1.asBoolean());

        YamlNode doubleNode2 = yamlNode("0.0");
        Assertions.assertNull(doubleNode2.asBoolean());

        YamlNode string1Node = yamlNode("\"false\"");
        Assertions.assertEquals(false, string1Node.asBoolean());

        YamlNode string2Node = yamlNode("\"true\"");
        Assertions.assertEquals(true, string2Node.asBoolean());

        YamlNode string3Node = yamlNode("\"t\"");
        Assertions.assertNull(string3Node.asBoolean());

        YamlNode objectNode = yamlNode("name: John\nage: 30");
        Assertions.assertNull(objectNode.asBoolean());

        YamlNode arrayNode = yamlNode("- item1\n- item2\n- item3");
        Assertions.assertNull(arrayNode.asBoolean());

    }


    @Test
    public void testAsInteger() throws IOException {
        YamlNode integerNode1 = yamlNode("42");
        Assertions.assertEquals(42, integerNode1.asInteger());

        YamlNode integerNode2 = yamlNode("-123");
        Assertions.assertEquals(-123, integerNode2.asInteger());

        YamlNode integerNode3 = yamlNode("0");
        Assertions.assertEquals(0, integerNode3.asInteger());

        YamlNode nullNode = yamlNode("null");
        Assertions.assertNull(nullNode.asInteger());

        YamlNode booleanNode1 = yamlNode("false");
        Assertions.assertEquals(0, booleanNode1.asInteger());

        YamlNode booleanNode2 = yamlNode("true");
        Assertions.assertEquals(1, booleanNode2.asInteger());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertEquals(22, doubleNode.asInteger());

        YamlNode string1Node = yamlNode("\"3\"");
        Assertions.assertEquals(3, string1Node.asInteger());

        YamlNode string2Node = yamlNode("\"10\"");
        Assertions.assertEquals(10, string2Node.asInteger());

        YamlNode string3Node = yamlNode("\"Hello, World!\"");
        Assertions.assertNull(string3Node.asInteger());

        YamlNode objectNode = yamlNode("name: John\nage: 30");
        Assertions.assertNull(objectNode.asInteger());

        YamlNode arrayNode = yamlNode("- item1\n- item2\n- item3");
        Assertions.assertNull(arrayNode.asInteger());
    }

    @Test
    public void testAsDouble() throws IOException {
        YamlNode doubleNode1 = yamlNode("22.1");
        Assertions.assertEquals(22.1, doubleNode1.asDouble());

        YamlNode doubleNode2 = yamlNode("-123.3");
        Assertions.assertEquals(-123.3, doubleNode2.asDouble());

        YamlNode doubleNode3 = yamlNode("0.0");
        Assertions.assertEquals(0.0, doubleNode3.asDouble());

        YamlNode nullNode = yamlNode("null");
        Assertions.assertNull(nullNode.asDouble());

        YamlNode booleanNode1 = yamlNode("false");
        Assertions.assertEquals(0.0, booleanNode1.asDouble());

        YamlNode booleanNode2 = yamlNode("true");
        Assertions.assertEquals(1.0, booleanNode2.asDouble());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertEquals(3.0, integerNode.asDouble());

        YamlNode string1Node = yamlNode("\"3\"");
        Assertions.assertEquals(3.0, string1Node.asDouble());

        YamlNode string2Node = yamlNode("\"10\"");
        Assertions.assertEquals(10.0, string2Node.asDouble());

        YamlNode string3Node = yamlNode("\"Hello, World!\"");
        Assertions.assertNull(string3Node.asInteger());

        YamlNode objectNode = yamlNode("name: John\nage: 30");
        Assertions.assertNull(objectNode.asInteger());

        YamlNode arrayNode = yamlNode("- item1\n- item2\n- item3");
        Assertions.assertNull(arrayNode.asInteger());
    }

    @Test
    public void testAsText() throws IOException {
        YamlNode stringNode = yamlNode("\"Hello, World!\"");
        Assertions.assertEquals("Hello, World!", stringNode.asText());

        YamlNode emptyStringNode = yamlNode("\"\"");
        Assertions.assertEquals("", emptyStringNode.asText());

        YamlNode whitespaceStringNode = yamlNode("\"    \"");
        Assertions.assertEquals("    ", whitespaceStringNode.asText());

        YamlNode nullNode = yamlNode("null");
        Assertions.assertEquals("", nullNode.asText());

        YamlNode booleanNode = yamlNode("false");
        Assertions.assertEquals("false", booleanNode.asText());

        YamlNode integerNode = yamlNode("3");
        Assertions.assertEquals("3", integerNode.asText());

        YamlNode doubleNode = yamlNode("22.1");
        Assertions.assertEquals("22.1", doubleNode.asText());

        YamlNode objectNode1 = yamlNode("key: value");
        Assertions.assertEquals("{\"key\":\"value\"}", objectNode1.asText());

        YamlNode objectNode2 = yamlNode("key1: value1\nkey2: value2");
        Assertions.assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", objectNode2.asText());

        YamlNode objectNode3 = yamlNode("key1: value1\nkey2:\n  - item1\n  - item2");
        Assertions.assertEquals("{\"key1\":\"value1\",\"key2\":[\"item1\",\"item2\"]}", objectNode3.asText());

        YamlNode arrayNode = yamlNode("- item1\n- item2");
        Assertions.assertEquals("[\"item1\",\"item2\"]", arrayNode.asText());
    }
}
