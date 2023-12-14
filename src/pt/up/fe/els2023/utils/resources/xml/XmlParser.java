package pt.up.fe.els2023.utils.resources.xml;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.up.fe.els2023.utils.resources.ResourceParser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class XmlParser implements ResourceParser {
    public XmlNode parse(Reader reader) throws IOException {
        var factory = DocumentBuilderFactory.newInstance();

        try {
            var doc = factory.newDocumentBuilder().parse(new InputSource(reader));

            return new XmlNode(doc.getDocumentElement(), doc.getDocumentElement().getTagName());

        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
    }
}
