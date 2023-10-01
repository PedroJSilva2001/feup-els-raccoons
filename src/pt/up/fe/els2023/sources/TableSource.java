package pt.up.fe.els2023.sources;

import java.util.List;

public abstract class TableSource {
    protected String name;
    protected List<String> files;

    public TableSource(String name, List<String> files) {
        this.name = name;
        this.files = files;
    }

    public abstract List<String> getRow(List<String> props);
}
