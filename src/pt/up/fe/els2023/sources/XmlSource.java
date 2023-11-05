package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.resources.ResourceParser;
import pt.up.fe.els2023.utils.resources.json.JsonParser;
import pt.up.fe.els2023.utils.resources.xml.XmlParser;

import java.util.List;

public class XmlSource extends TableSource {
    public XmlSource(String name, List<String> files) {
        super(name, files);
    }

    @Override
    public ResourceParser getResourceParser() {
        return new XmlParser();
    }
}