package pt.up.fe.els2023.dsl;

import pt.up.fe.els2023.table.schema.*;

import java.util.List;
import java.util.Set;

public class SchemaNodeFactory {
    public static AllContainerNode allContainer() {
        return new AllContainerNode();
    }

    public static AllContainerNode allContainer(String format) {
        return new AllContainerNode(format);
    }

    public static AllNode all() {
        return new AllNode();
    }

    public static AllNode all(String format) {
        return new AllNode(format);
    }

    public static AllValueNode allValue() {
        return new AllValueNode();
    }

    public static AllValueNode allValue(String format) {
        return new AllValueNode(format);
    }
    public static ColumnNode column(String columnName) {
        return new ColumnNode(columnName);
    }

    public static DirectoryNode directory(String columnName) {
        return new DirectoryNode(columnName);
    }

    public static DirectoryNode directory() {
        return new DirectoryNode();
    }

    public static EachNode each(SchemaNode value) {
        return new EachNode(value);
    }

    public static EachNode each(SchemaNode... values) {
        return new EachNode(new ListNode(values));
    }

    public static EachNode each(String columnName) {
        return new EachNode(new ColumnNode(columnName));
    }

    public static EachNode each() {
        return new EachNode(new NullNode());
    }

    // TODO: Possibly except could be a SchemaNode, which would specify the schema of the properties to exclude.
    // TODO: This would be useful for specifying a certain index of an array, for example.
    public static ExceptNode except(String... except) {
        return new ExceptNode(Set.of(except));
    }

    public static ExceptNode exceptF(String format, String... except) {
        return new ExceptNode(Set.of(except), format);
    }

    public static FileNode file(String columnName) {
        return new FileNode(columnName);
    }

    public static FileNode file() {
        return new FileNode();
    }

    public static IndexNode index(int index, SchemaNode value) {
        return new IndexNode(index, value);
    }

    public static IndexNode index(int index, SchemaNode... values) {
        return new IndexNode(index, new ListNode(values));
    }

    public static IndexNode index(int index, String columnName) {
        return new IndexNode(index, new ColumnNode(columnName));
    }

    public static IndexNode index(int index) {
        return new IndexNode(index, new NullNode());
    }

    public static IndexOfNode indexOf(int index, String keyName, SchemaNode value) {
        return new IndexOfNode(index, keyName, value);
    }

    public static IndexOfNode indexOf(int index, String keyName, SchemaNode... values) {
        return new IndexOfNode(index, keyName, new ListNode(values));
    }

    public static IndexOfNode indexOf(int index, String keyName, String columnName) {
        return new IndexOfNode(index, keyName, new ColumnNode(columnName));
    }

    public static IndexOfNode indexOf(int index, String keyName) {
        return new IndexOfNode(index, keyName, new NullNode());
    }

    public static ListNode list(SchemaNode... nodes) {
        return new ListNode(List.of(nodes));
    }

    public static NullNode nullNode() {
        return new NullNode();
    }

    public static PathNode path(String columnName) {
        return new PathNode(columnName);
    }

    public static PathNode path() {
        return new PathNode();
    }

    public static PropertyNode property(String keyName, SchemaNode value) {
        return new PropertyNode(keyName, value);
    }

    public static PropertyNode property(String keyName, SchemaNode... values) {
        return new PropertyNode(keyName, new ListNode(values));
    }

    public static PropertyNode property(String keyName, String columnName) {
        return new PropertyNode(keyName, new ColumnNode(columnName));
    }

    public static PropertyNode property(String keyName) {
        return new PropertyNode(keyName, new NullNode());
    }
}
