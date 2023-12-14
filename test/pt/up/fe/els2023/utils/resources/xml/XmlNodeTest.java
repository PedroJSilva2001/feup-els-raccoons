package pt.up.fe.els2023.utils.resources.xml;

import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.utils.resources.yaml.YamlNode;
import pt.up.fe.els2023.utils.resources.yaml.YamlParser;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class XmlNodeTest {

    private XmlParser parser = new XmlParser();

    private XmlNode xmlNode(String s) throws IOException {
        return parser.parse(new StringReader(s));
    }
    @Test
    void isContainer() throws IOException {
        String xml = "<root><a>1</a></root>";

        XmlNode node = xmlNode(xml);
        assertTrue(node.isContainer());

        String xml2 = "<root><a>1</a><b>2</b></root>";

        XmlNode node2 = xmlNode(xml2);
        assertTrue(node2.isContainer());

        String xml3 = "<root></root>";

        XmlNode node3 = xmlNode(xml3);
        assertFalse(node3.isContainer());

        String xml4 = "<root>2</root>";

        XmlNode node4 = xmlNode(xml4);
        assertFalse(node4.isContainer());

        String xml5 = """
                <root>
                    3
                </root>
                """;

        XmlNode node5 = xmlNode(xml5);
        assertFalse(node5.isContainer());
    }
}