package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.resources.ResourceParser;
import pt.up.fe.els2023.utils.resources.yaml.YamlParser;

import java.util.List;

public class YamlSource extends TableSource {
    public YamlSource(String name, List<String> files) {
        super(name, files);
    }

    @Override
    public ResourceParser getResourceParser() {
        return new YamlParser();
    }
}
