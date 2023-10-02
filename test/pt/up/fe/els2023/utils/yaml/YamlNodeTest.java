package pt.up.fe.els2023.utils.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

public class YamlNodeTest {

    /*private YamlNode yamlNode(String s) throws IOException {
        return null
    }*/
    
    /*
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isNull());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isNull());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isBoolean());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isBoolean());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isInteger());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isInteger());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isDouble());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isDouble());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isText());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isText());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertTrue(mapNode.isObject());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isObject());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isArray());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertTrue(listNode.isArray());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isValue());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isValue());
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

        YamlNode mapNode = yamlNode("{\"key\": \"value\"}");
        Assertions.assertTrue(mapNode.isContainer());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\"]");
        Assertions.assertTrue(listNode.isContainer());
    }


    @Test
    public void testGetStringProperty() throws IOException {
        YamlNode mapNode = yamlNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertEquals("John", mapNode.get("name").asText());
        Assertions.assertEquals(30, mapNode.get("age").asInteger());
        Assertions.assertNull(mapNode.get("address"));

        YamlNode listNode = yamlNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.get("name"));
        Assertions.assertNull(listNode.get("age"));

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
        YamlNode listNode = yamlNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertEquals("item1", listNode.get(0).asText());
        Assertions.assertEquals("item2", listNode.get(1).asText());
        Assertions.assertEquals("item3", listNode.get(2).asText());
        Assertions.assertNull(listNode.get(-1));
        Assertions.assertNull(listNode.get(3));

        YamlNode mapNode = yamlNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.get(0));
        Assertions.assertNull(mapNode.get(-1));

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

        YamlNode mapNode = yamlNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asBoolean());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asBoolean());

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

        YamlNode mapNode = yamlNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asInteger());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asInteger());
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

        YamlNode mapNode = yamlNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asInteger());

        YamlNode listNode = yamlNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asInteger());
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

        YamlNode mapNode1 = yamlNode("{\"key\":\"value\"}");
        Assertions.assertEquals("{\"key\":\"value\"}", mapNode1.asText());


        YamlNode mapNode2 = yamlNode("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        Assertions.assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", mapNode2.asText());

        YamlNode mapNode3 = yamlNode("{\"key1\":\"value1\",\"key2\":[\"item1\",\"item2\"]}");
        Assertions.assertEquals("{\"key1\":\"value1\",\"key2\":[\"item1\",\"item2\"]}", mapNode3.asText());

        YamlNode listNode = yamlNode("[\"item1\",\"item2\"]");
        Assertions.assertEquals("[\"item1\",\"item2\"]", listNode.asText());
    }*/
}
