package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.table.Value;
import pt.up.fe.els2023.utils.resources.ResourceNode;

import java.nio.file.Path;
import java.util.*;


/**
 * The PopulateVisitor class is responsible for traversing and populating data
 * from a ResourceNode into tables, following a provided schema defined by SchemaNode objects.
 */
public class PopulateVisitor implements NodeVisitor {
    /**
     * This stack is used to keep track of the current ResourceNode
     * and the name of the column it maps to if a NullNode is
     * encountered.
     */
    private final Stack<TraversingInfo> traversingStack;
    private final NodeColumnMap nodeColumnMap;
    /**
     * This map is used to keep track of the values of the current row.
     */
    private final Map<String, Value> rowValues = new HashMap<>();
    /**
     * This map is used to keep track of the results of EachNode.
     * The columns that EachNode maps to are stored in this map,
     * acting as a way to store intermediate results/tables.
     * <p>
     * Since column names are unique and assigned to a single SchemaNode,
     * the results of EachNode are the final values of the column.
     */
    private final Map<String, List<Value>> columnValues = new HashMap<>();
    private Path path;

    private PopulateVisitor(NodeColumnMap nodeColumnMap, Stack<TraversingInfo> traversingStack) {
        this.nodeColumnMap = nodeColumnMap;
        this.traversingStack = traversingStack;
    }

    public PopulateVisitor(NodeColumnMap nodeColumnMap) {
        this.nodeColumnMap = nodeColumnMap;
        this.traversingStack = new Stack<>();
    }

    public PopulateVisitor() {
        this.nodeColumnMap = new NodeColumnMap();
        this.traversingStack = new Stack<>();
    }

    public NodeColumnMap getNodeColumnMap() {
        return nodeColumnMap;
    }

    /**
     * Populates data from a ResourceNode into tables, following a provided schema defined by SchemaNode objects.
     *
     * @param rootNode    The root node of the data.
     * @param schemaNodes The schema nodes that define the mapping of the data to the table.
     * @return A map of column names to values.
     */
    public Map<String, List<Value>> populateFromSource(Path path, ResourceNode rootNode, List<SchemaNode> schemaNodes) {
        boolean emptyStack = this.traversingStack.empty();
        this.path = path;

        if (emptyStack) {
            this.traversingStack.push(new TraversingInfo(rootNode, "$root"));
        }

        for (var node : schemaNodes) {
            node.accept(this);
        }

        if (emptyStack) {
            this.traversingStack.pop();
        }

        return merge();
    }

    private Value getValueFromNode(ResourceNode node) {
        if (node.isValue()) {
            if (node.isBoolean()) {
                return Value.of(node.asBoolean());
            } else if (node.isLong()) {
                return Value.of(node.asLong());
            } else if (node.isDouble()) {
                return Value.of(node.asDouble());
            } else if (node.isBigInteger()) {
                return Value.of(node.asBigInteger());
            } else if (node.isBigDecimal()) {
                return Value.of(node.asBigDecimal());
            } else if (node.isNull()) {
                return Value.ofNull();
            }
        }

        return Value.of(node.asText());
    }

    /**
     * Combines the row values and column values into a unified map.
     * <p>
     * This process involves ensuring that all columns have the same size as the number of rows.
     * If a column has fewer values than the maximum row count, it's padded with null values.
     * Then, the row values are multiplied for each row.
     *
     * @return A map where column names are associated with corresponding values.
     */
    private Map<String, List<Value>> merge() {
        int maxColumnSize = rowValues.values().stream().anyMatch(obj -> !obj.isNull()) ? 1 : 0;

        for (var column : columnValues.entrySet()) {
            maxColumnSize = Math.max(maxColumnSize, column.getValue().size());
        }

        Map<String, List<Value>> merged = new HashMap<>();

        for (var column : columnValues.entrySet()) {
            // Pad with nulls
            List<Value> values = new ArrayList<>(Collections.nCopies(maxColumnSize, Value.ofNull()));

            for (int i = 0; i < column.getValue().size(); i++) {
                values.set(i, column.getValue().get(i));
            }

            merged.put(column.getKey(), values);
        }

        for (var row : rowValues.entrySet()) {
            merged.put(row.getKey(), new ArrayList<>(Collections.nCopies(maxColumnSize, row.getValue())));
        }

        return merged;
    }

    @Override
    public void visit(AllContainerNode node) {
        TraversingInfo info = this.traversingStack.peek();

        // TODO: Think about supporting arrays as well
        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children) {
            if (child.isContainer()) {
                String propertyName = child.getName();
                String columnName = ColumnUtils.makeUnique(propertyName, properties);

                properties.add(columnName);
                columnName = nodeColumnMap.add(node, columnName);

                rowValues.put(columnName, getValueFromNode(child));
            }
        }
    }

    @Override
    public void visit(AllNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children) {
            String propertyName = child.getName();
            String columnName = ColumnUtils.makeUnique(propertyName, properties);

            properties.add(columnName);
            columnName = nodeColumnMap.add(node, columnName);

            rowValues.put(columnName, getValueFromNode(child));
        }
    }

    @Override
    public void visit(AllValueNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children) {
            if (child.isValue()) {
                String propertyName = child.getName();
                String columnName = ColumnUtils.makeUnique(propertyName, properties);

                properties.add(columnName);
                columnName = nodeColumnMap.add(node, columnName);

                rowValues.put(columnName, getValueFromNode(child));
            }
        }
    }

    @Override
    public void visit(PropertyNode node) {
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.keyName());

        if (child == null) {
            return;
        }

        this.traversingStack.push(new TraversingInfo(child, node.keyName()));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(ColumnNode node) {
        TraversingInfo info = this.traversingStack.peek();
        String columnName = nodeColumnMap.add(node, node.columnName());

        rowValues.put(columnName, getValueFromNode(info.node));
    }

    @Override
    public void visit(EachNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isArray()) {
            return;
        }

        // Iterate over each resource node in the array
        for (var resourceNode : info.node) {
            traversingStack.push(new TraversingInfo(resourceNode, info.property));

            // Recursively populate the columns specified by the EachNode
            PopulateVisitor visitor = new PopulateVisitor(nodeColumnMap, traversingStack);
            Map<String, List<Value>> columns = visitor.populateFromSource(this.path, resourceNode, List.of(node.value()));

            // Concatenate the results of the EachNode vertically
            for (var column : columns.entrySet()) {
                if (!columnValues.containsKey(column.getKey())) {
                    columnValues.put(column.getKey(), new ArrayList<>());
                }

                columnValues.get(column.getKey()).addAll(column.getValue());
            }

            traversingStack.pop();
        }
    }

    @Override
    public void visit(ExceptNode node) {
        TraversingInfo info = this.traversingStack.peek();

        if (!info.node.isObject()) {
            return;
        }

        var children = info.node.getChildren();
        Set<String> properties = new HashSet<>();

        for (var child : children) {
            if (!node.except().contains(child.getName())) {
                String propertyName = child.getName();
                String columnName = ColumnUtils.makeUnique(propertyName, properties);

                properties.add(columnName);
                columnName = nodeColumnMap.add(node, columnName);

                rowValues.put(columnName, getValueFromNode(child));
            }
        }
    }

    @Override
    public void visit(IndexNode node) {
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.index());

        if (child == null) {
            return;
        }

        this.traversingStack.push(new TraversingInfo(child, info.property + "[" + node.index() + "]"));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(IndexOfNode node) {
        // TODO: I'm not sure how ResourceNode works with XML, so this might not work
        // TODO: For now, this is just a copy of IndexNode
        TraversingInfo info = this.traversingStack.peek();
        ResourceNode child = info.node.get(node.index());

        if (child == null) {
            return;
        }

        this.traversingStack.push(new TraversingInfo(child, info.property + "[" + node.index() + "]"));

        node.value().accept(this);

        this.traversingStack.pop();
    }

    @Override
    public void visit(ListNode node) {
        for (var childNode : node.list()) {
            childNode.accept(this);
        }
    }

    @Override
    public void visit(NullNode node) {
        TraversingInfo info = this.traversingStack.peek();

        String columnName = nodeColumnMap.add(node, info.property);
        rowValues.put(columnName, getValueFromNode(info.node));
    }

    @Override
    public void visit(DirectoryNode node) {
        String columnName = nodeColumnMap.add(node, node.columnName());
        rowValues.put(columnName, Value.of(path.getParent().getFileName().toString()));
    }

    @Override
    public void visit(FileNode node) {
        String columnName = nodeColumnMap.add(node, node.columnName());
        rowValues.put(columnName, Value.of(path.getFileName().toString()));
    }

    @Override
    public void visit(PathNode node) {
        String columnName = nodeColumnMap.add(node, node.columnName());
        rowValues.put(columnName, Value.of(path.toString()));
    }

    private record TraversingInfo(ResourceNode node, String property) {
    }
}
