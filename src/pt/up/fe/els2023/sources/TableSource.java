package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.utils.resources.ResourceParser;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableSource that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(files, that.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, files);
    }
}
