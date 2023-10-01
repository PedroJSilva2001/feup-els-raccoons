package pt.up.fe.els2023.sources;

import java.util.List;

public class JsonSource extends TableSource {
    public JsonSource(String name, List<String> files) {
        super(name, files);
    }

    @Override
    public List<String> getRow(List<String> props) {
        return null;
    }
}
