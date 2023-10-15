package pt.up.fe.els2023.config;

import pt.up.fe.els2023.sources.TableSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TableSchema {
    private String name;
    private List<SchemaNode> from;
    private TableSource source;

    public TableSchema(
            String name
    ) {
        this.name = name;
        this.from = new ArrayList<>();
    }

    public TableSchema name(String name) {
        this.name = name;
        return this;
    }

    public TableSchema from(List<SchemaNode> from) {
        this.from = from;
        return this;
    }

    public List<SchemaNode> from() {
        return Collections.unmodifiableList(from);
    }

    public String name() {
        return name;
    }

    public TableSource source() {
        return source;
    }

    public TableSchema source(TableSource source) {
        this.source = source;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TableSchema) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.from, that.from) &&
                Objects.equals(this.source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, from, source);
    }
}
