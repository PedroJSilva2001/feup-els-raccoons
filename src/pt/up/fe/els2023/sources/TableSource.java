package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.resources.ResourceParser;

import java.util.List;

public abstract class TableSource {
    protected String name;

    protected List<String> files;

    public TableSource(String name, List<String> files) {
        this.name = name;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public List<String> getFiles() {
        return files;
    }

    public abstract ResourceParser getResourceParser();
}
