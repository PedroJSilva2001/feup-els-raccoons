package pt.up.fe.els2023.config;

import pt.up.fe.els2023.imports.NodeOrderVisitor;
import pt.up.fe.els2023.imports.PopulateVisitor;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;
import pt.up.fe.els2023.utils.GlobFinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

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

    private Set<Path> getFiles() {
        List<String> globs = source.getFiles();
        Set<Path> files = new LinkedHashSet<>();

        for (String glob : globs) {
            try {
                files.addAll(GlobFinder.getFilesGlob(glob));
            } catch (Exception e) {
                System.err.println("Error while collecting files from glob " + glob);
                e.printStackTrace();
            }
        }

        return files;
    }

    // it isn't pretty to alter the maps in place, but it should be faster than creating new maps
    private void concatColumnMaps(Map<String, List<Value>> previousMap, Map<String, List<Value>> currentMap) {
        // we assume that all columns have the same height, which should be enforced by the parser
        // hopefully :) (needs some testing)
        int columnHeight = previousMap.isEmpty() ? 0 : previousMap.values().iterator().next().size();
        int newColumnHeight = columnHeight;

        for (Map.Entry<String, List<Value>> entry : currentMap.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            if (previousMap.containsKey(key)) {
                previousMap.get(key).addAll(value);
            } else {
                List<Value> column = new ArrayList<>(Collections.nCopies(columnHeight, Value.ofNull()));
                column.addAll(value);

                previousMap.put(key, column);
            }

            newColumnHeight = Math.max(newColumnHeight, previousMap.get(key).size());
        }

        // fill in the blanks
        for (var value : previousMap.values()) {
            if (value.size() < newColumnHeight) {
                value.addAll(Collections.nCopies(newColumnHeight - value.size(), Value.ofNull()));
            }
        }
    }

    private Table mapToTable(Map<String, List<Value>> columnMap, List<String> columnOrder) {
        Table table = new Table(this.name);

        for (String columnName : columnOrder) {
            table.addColumn(columnName);
        }

        int columnHeight = columnMap.isEmpty() ? 0 : columnMap.values().iterator().next().size();

        for (int i = 0; i < columnHeight; i++) {
            List<Value> row = new ArrayList<>();

            for (String columnName : columnOrder) {
                row.add(columnMap.get(columnName).get(i));
            }

            table.addRow(row);
        }

        return table;
    }

    public Table collect() {
        var orderVisitor = new NodeOrderVisitor();

        var nodeOrder = orderVisitor.getNodeOrder(this.nft);
        var columnMap = new HashMap<String, List<Value>>();
        var files = getFiles();

        var visitor = new PopulateVisitor();
        for (Path file : files) {
            try (var fileReader = new FileReader(file.toFile())) {
                var reader = new BufferedReader(fileReader);
                var rootNode = this.source.getResourceParser().parse(reader);

                var fileColumnMap = visitor.populateFromSource(file, rootNode, this.nft);
                concatColumnMaps(columnMap, fileColumnMap);

                visitor = new PopulateVisitor(visitor.getNodeColumnMap());
            } catch (IOException e) {
                System.err.println("Error while reading file " + file);
                e.printStackTrace();
            }
        }

        var nodeColumnMap = visitor.getNodeColumnMap();
        var columnOrder = nodeColumnMap.getOrderedColumnNames(nodeOrder);

        return mapToTable(columnMap, columnOrder);
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
