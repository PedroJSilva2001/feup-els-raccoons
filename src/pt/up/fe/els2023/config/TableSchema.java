package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.PopulateVisitor;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.utils.GlobFinder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a table schema for data mapping.
 * <p>
 * The table schema describes the structure of the table, including the name of the table, how sources are mapped to
 * the table, and the source of the table.
 */
public final class TableSchema {
    private String name;
    private List<SchemaNode> nft;
    private TableSource source;

    public TableSchema(
            String name
    ) {
        this.name = name;
        this.nft = new ArrayList<>();
    }

    public TableSchema name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the nodes that define the mapping of the object to the table.
     * <p>
     * NFT stands for Nested Field Traversal. The nodes are traversed in depth-first order, and columns
     * will be in the order defined by the nodes.
     *
     * @param from The nodes that define the mapping of the object to the table.
     * @return This object.
     */
    public TableSchema nft(List<SchemaNode> from) {
        this.nft = from;
        return this;
    }

    /**
     * Sets the nodes that define the mapping of the object to the table.
     * <p>
     * NFT stands for Nested Field Traversal. The nodes are traversed in depth-first order, and columns
     * will be in the order defined by the nodes.
     *
     * @param from The nodes that define the mapping of the object to the table.
     * @return This object.
     */
    public TableSchema nft(SchemaNode... from) {
        this.nft = List.of(from);
        return this;
    }

    public List<SchemaNode> nft() {
        return Collections.unmodifiableList(nft);
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

    public Table collect() {
        List<String> globs = source.getFiles();
        List<Path> files = new ArrayList<>();

        for (String glob : globs) {
            try {
                files.addAll(GlobFinder.getFilesGlob(glob));
            } catch (Exception e) {
                System.err.println("Error while collecting files from glob " + glob);
                e.printStackTrace();
            }
        }

        PopulateVisitor visitor = new PopulateVisitor();
        for (Path file : files) {
            //visitor.popul


        }

        return new Table("1", source);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TableSchema) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.nft, that.nft) &&
                Objects.equals(this.source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nft, source);
    }
}
