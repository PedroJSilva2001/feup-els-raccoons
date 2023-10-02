package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.resources.ResourceParser;
import pt.up.fe.els2023.utils.resources.json.JsonParser;

import java.util.List;

public class JsonSource extends TableSource {
    public JsonSource(String name, List<String> files) {
        super(name, files);
    }

    @Override
    public ResourceParser getResourceParser() {
        return new JsonParser();
    }
}