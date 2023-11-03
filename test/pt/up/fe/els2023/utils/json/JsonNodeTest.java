package pt.up.fe.els2023.utils.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.utils.resources.json.JsonNode;
import pt.up.fe.els2023.utils.resources.json.JsonParser;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;

public class JsonNodeTest {

    private final JsonParser parser = new JsonParser();

    private JsonNode jsonNode(String s) throws IOException {
        return parser.parse(new StringReader(s));
    }

    @Test
    public void testIsNull() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertTrue(nullNode.isNull());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isNull());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isNull());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isNull());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isNull());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isNull());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isNull());
    }

    @Test
    public void testIsBoolean() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isBoolean());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertTrue(booleanNode.isBoolean());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isBoolean());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isBoolean());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isBoolean());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isBoolean());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isBoolean());
    }

    @Test
    public void testIsInteger() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isInteger());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isInteger());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertTrue(integerNode.isInteger());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isInteger());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isInteger());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isInteger());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isInteger());

        JsonNode longNode = jsonNode("9223372036854775807");
        Assertions.assertFalse(longNode.isInteger());

        JsonNode bigIntegerNode = jsonNode("9223372036854775808");
        Assertions.assertFalse(bigIntegerNode.isInteger());
    }

    @Test
    public void testIsLong() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isLong());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isLong());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertTrue(integerNode.isLong());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isLong());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isLong());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isLong());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isLong());

        JsonNode longNode = jsonNode("9223372036854775807");
        Assertions.assertTrue(longNode.isLong());

        JsonNode bigIntegerNode = jsonNode("9223372036854775808");
        Assertions.assertFalse(bigIntegerNode.isLong());
    }

    @Test
    public void testIsDouble() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isDouble());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isDouble());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isDouble());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertTrue(doubleNode.isDouble());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isDouble());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isDouble());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isDouble());

        JsonNode bigDecimalNode = jsonNode(BigDecimal.valueOf(Double.MAX_VALUE).toPlainString());
        Assertions.assertFalse(bigDecimalNode.isDouble());
    }

    @Test
    public void testIsBigDecimal() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isBigDecimal());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isBigDecimal());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isBigDecimal());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isBigDecimal());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isBigDecimal());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isBigDecimal());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isBigDecimal());

        System.out.println(BigDecimal.valueOf(Double.MAX_VALUE).toPlainString());
        JsonNode bigDecimalNode = jsonNode("111111111111" + BigDecimal.valueOf(Double.MAX_VALUE).toPlainString());
    }

    @Test
    public void testIsText() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isText());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isText());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isText());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isText());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertTrue(stringNode.isText());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isText());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isText());
    }

    @Test
    public void testIsObject() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isObject());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isObject());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isObject());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isObject());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isObject());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertTrue(mapNode.isObject());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isObject());
    }

    @Test
    public void testIsArray() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isArray());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertFalse(booleanNode.isArray());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isArray());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isArray());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isArray());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isArray());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertTrue(listNode.isArray());
    }

    @Test
    public void testIsValue() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertTrue(nullNode.isValue());

        JsonNode booleanNode1 = jsonNode("false");
        Assertions.assertTrue(booleanNode1.isValue());

        JsonNode booleanNode2 = jsonNode("true");
        Assertions.assertTrue(booleanNode2.isValue());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertTrue(integerNode.isValue());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertTrue(doubleNode.isValue());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertTrue(stringNode.isValue());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertFalse(mapNode.isValue());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertFalse(listNode.isValue());
    }

    @Test
    public void testIsContainer() throws IOException {
        JsonNode nullNode = jsonNode("null");
        Assertions.assertFalse(nullNode.isContainer());

        JsonNode booleanNode1 = jsonNode("false");
        Assertions.assertFalse(booleanNode1.isContainer());

        JsonNode booleanNode2 = jsonNode("true");
        Assertions.assertFalse(booleanNode2.isContainer());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertFalse(integerNode.isContainer());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertFalse(doubleNode.isContainer());

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertFalse(stringNode.isContainer());

        JsonNode mapNode = jsonNode("{\"key\": \"value\"}");
        Assertions.assertTrue(mapNode.isContainer());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\"]");
        Assertions.assertTrue(listNode.isContainer());
    }


    @Test
    public void testGetStringProperty() throws IOException {
        JsonNode mapNode = jsonNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertEquals("John", mapNode.get("name").asText());
        Assertions.assertEquals(30, mapNode.get("age").asInteger());
        Assertions.assertNull(mapNode.get("address"));

        JsonNode listNode = jsonNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.get("name"));
        Assertions.assertNull(listNode.get("age"));

        JsonNode nullNode = jsonNode("null");
        Assertions.assertNull(nullNode.get("name"));
        Assertions.assertNull(nullNode.get("age"));

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertNull(booleanNode.get("name"));
        Assertions.assertNull(booleanNode.get("age"));

        JsonNode integerNode = jsonNode("3");
        Assertions.assertNull(integerNode.get("name"));
        Assertions.assertNull(integerNode.get("age"));

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertNull(doubleNode.get("name"));
        Assertions.assertNull(doubleNode.get("age"));

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertNull(stringNode.get("name"));
        Assertions.assertNull(stringNode.get("age"));
    }

    @Test
    public void testGetIndexedItem() throws IOException {
        JsonNode listNode = jsonNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertEquals("item1", listNode.get(0).asText());
        Assertions.assertEquals("item2", listNode.get(1).asText());
        Assertions.assertEquals("item3", listNode.get(2).asText());
        Assertions.assertNull(listNode.get(-1));
        Assertions.assertNull(listNode.get(3));

        JsonNode mapNode = jsonNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.get(0));
        Assertions.assertNull(mapNode.get(-1));

        JsonNode nullNode = jsonNode("null");
        Assertions.assertNull(nullNode.get(0));
        Assertions.assertNull(nullNode.get(-1));

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertNull(booleanNode.get(0));
        Assertions.assertNull(booleanNode.get(-1));

        JsonNode integerNode = jsonNode("3");
        Assertions.assertNull(integerNode.get(0));
        Assertions.assertNull(integerNode.get(-1));

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertNull(doubleNode.get(0));
        Assertions.assertNull(doubleNode.get(-1));

        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertNull(stringNode.get(0));
        Assertions.assertNull(stringNode.get(-1));
    }

    @Test
    public void testGetNestedProperty() throws IOException {
        String jsonData = "{ \"prop1\": 1, \"prop2\": {\"prop3\": \"val3\", \"prop4\": [1,2,3]}, \"prop5\": null }";

        JsonNode rootNode = jsonNode(jsonData);


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
        String jsonData1 = "{\"prop1\": [1,2,3]}";

        JsonNode rootNode1 = jsonNode(jsonData1);

        String jsonData2 = "[1,2,3]";

        JsonNode rootNode2 = jsonNode(jsonData2);

        // Test getting a property where an array is encountered (unsupported)
        String result = rootNode1.getNested("prop1.item1");
        Assertions.assertNull(result);

        result = rootNode2.getNested("item1");
        Assertions.assertNull(result);
    }


    @Test
    public void testAsBoolean() throws IOException {
        JsonNode booleanNode1 = jsonNode("false");
        Assertions.assertEquals(false, booleanNode1.asBoolean());

        JsonNode booleanNode2 = jsonNode("true");
        Assertions.assertEquals(true, booleanNode2.asBoolean());

        JsonNode nullNode = jsonNode("null");
        Assertions.assertNull(nullNode.asBoolean());

        JsonNode integerNode1 = jsonNode("3");
        Assertions.assertEquals(true, integerNode1.asBoolean());

        JsonNode integerNode2 = jsonNode("1");
        Assertions.assertEquals(true, integerNode2.asBoolean());

        JsonNode integerNode3 = jsonNode("0");
        Assertions.assertEquals(false, integerNode3.asBoolean());


        JsonNode doubleNode1 = jsonNode("22.1");
        Assertions.assertNull(doubleNode1.asBoolean());

        JsonNode doubleNode2 = jsonNode("0.0");
        Assertions.assertNull(doubleNode2.asBoolean());

        JsonNode string1Node = jsonNode("\"false\"");
        Assertions.assertEquals(false, string1Node.asBoolean());

        JsonNode string2Node = jsonNode("\"true\"");
        Assertions.assertEquals(true, string2Node.asBoolean());

        JsonNode string3Node = jsonNode("\"t\"");
        Assertions.assertNull(string3Node.asBoolean());

        JsonNode mapNode = jsonNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asBoolean());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asBoolean());

    }


    @Test
    public void testAsInteger() throws IOException {
        JsonNode integerNode1 = jsonNode("42");
        Assertions.assertEquals(42, integerNode1.asInteger());

        JsonNode integerNode2 = jsonNode("-123");
        Assertions.assertEquals(-123, integerNode2.asInteger());

        JsonNode integerNode3 = jsonNode("0");
        Assertions.assertEquals(0, integerNode3.asInteger());

        JsonNode nullNode = jsonNode("null");
        Assertions.assertNull(nullNode.asInteger());

        JsonNode booleanNode1 = jsonNode("false");
        Assertions.assertEquals(0, booleanNode1.asInteger());

        JsonNode booleanNode2 = jsonNode("true");
        Assertions.assertEquals(1, booleanNode2.asInteger());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertEquals(22, doubleNode.asInteger());

        JsonNode string1Node = jsonNode("\"3\"");
        Assertions.assertEquals(3, string1Node.asInteger());

        JsonNode string2Node = jsonNode("\"10\"");
        Assertions.assertEquals(10, string2Node.asInteger());

        JsonNode string3Node = jsonNode("\"Hello, World!\"");
        Assertions.assertNull(string3Node.asInteger());

        JsonNode mapNode = jsonNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asInteger());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asInteger());

        JsonNode longNode = jsonNode("9223372036854775807");
        Assertions.assertNull(longNode.asInteger());
    }

    @Test
    public void testAsLong() throws IOException {
        JsonNode integerNode1 = jsonNode("42");
        Assertions.assertEquals(42, integerNode1.asLong());

        JsonNode integerNode2 = jsonNode("-123");
        Assertions.assertEquals(-123, integerNode2.asLong());

        JsonNode integerNode3 = jsonNode("0");
        Assertions.assertEquals(0, integerNode3.asLong());

        JsonNode nullNode = jsonNode("null");
        Assertions.assertNull(nullNode.asLong());

        JsonNode booleanNode1 = jsonNode("false");
        Assertions.assertEquals(0, booleanNode1.asLong());

        JsonNode booleanNode2 = jsonNode("true");
        Assertions.assertEquals(1, booleanNode2.asLong());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertEquals(22, doubleNode.asLong());

        JsonNode string1Node = jsonNode("\"3\"");
        Assertions.assertEquals(3, string1Node.asLong());

        JsonNode string2Node = jsonNode("\"10\"");
        Assertions.assertEquals(10, string2Node.asLong());

        JsonNode string3Node = jsonNode("\"Hello, World!\"");
        Assertions.assertNull(string3Node.asLong());

        JsonNode mapNode = jsonNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asLong());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asLong());

        JsonNode longNode = jsonNode("9223372036854775807");
        Assertions.assertEquals(9223372036854775807L, longNode.asLong());
    }

    @Test
    public void testAsDouble() throws IOException {
        JsonNode doubleNode1 = jsonNode("22.1");
        Assertions.assertEquals(22.1, doubleNode1.asDouble());

        JsonNode doubleNode2 = jsonNode("-123.3");
        Assertions.assertEquals(-123.3, doubleNode2.asDouble());

        JsonNode doubleNode3 = jsonNode("0.0");
        Assertions.assertEquals(0.0, doubleNode3.asDouble());

        JsonNode nullNode = jsonNode("null");
        Assertions.assertNull(nullNode.asDouble());

        JsonNode booleanNode1 = jsonNode("false");
        Assertions.assertEquals(0.0, booleanNode1.asDouble());

        JsonNode booleanNode2 = jsonNode("true");
        Assertions.assertEquals(1.0, booleanNode2.asDouble());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertEquals(3.0, integerNode.asDouble());

        JsonNode string1Node = jsonNode("\"3\"");
        Assertions.assertEquals(3.0, string1Node.asDouble());

        JsonNode string2Node = jsonNode("\"10\"");
        Assertions.assertEquals(10.0, string2Node.asDouble());

        JsonNode string3Node = jsonNode("\"Hello, World!\"");
        Assertions.assertNull(string3Node.asInteger());

        JsonNode mapNode = jsonNode("{\"name\": \"John\", \"age\": 30}");
        Assertions.assertNull(mapNode.asInteger());

        JsonNode listNode = jsonNode("[\"item1\", \"item2\", \"item3\"]");
        Assertions.assertNull(listNode.asInteger());
    }

    @Test
    public void testAsText() throws IOException {
        JsonNode stringNode = jsonNode("\"Hello, World!\"");
        Assertions.assertEquals("Hello, World!", stringNode.asText());

        JsonNode emptyStringNode = jsonNode("\"\"");
        Assertions.assertEquals("", emptyStringNode.asText());

        JsonNode whitespaceStringNode = jsonNode("\"    \"");
        Assertions.assertEquals("    ", whitespaceStringNode.asText());

        JsonNode nullNode = jsonNode("null");
        Assertions.assertEquals("", nullNode.asText());

        JsonNode booleanNode = jsonNode("false");
        Assertions.assertEquals("false", booleanNode.asText());

        JsonNode integerNode = jsonNode("3");
        Assertions.assertEquals("3", integerNode.asText());

        JsonNode doubleNode = jsonNode("22.1");
        Assertions.assertEquals("22.1", doubleNode.asText());

        JsonNode mapNode1 = jsonNode("{\"key\":\"value\"}");
        Assertions.assertEquals("{\"key\":\"value\"}", mapNode1.asText());


        JsonNode mapNode2 = jsonNode("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        Assertions.assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", mapNode2.asText());

        JsonNode mapNode3 = jsonNode("{\"key1\":\"value1\",\"key2\":[\"item1\",\"item2\"]}");
        Assertions.assertEquals("{\"key1\":\"value1\",\"key2\":[\"item1\",\"item2\"]}", mapNode3.asText());

        JsonNode listNode = jsonNode("[\"item1\",\"item2\"]");
        Assertions.assertEquals("[\"item1\",\"item2\"]", listNode.asText());
    }
}
