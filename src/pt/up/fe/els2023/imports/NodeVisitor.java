package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;

/**
 * A visitor for schema nodes.
 */
public interface NodeVisitor {
    void visit(AllContainerNode node);

    void visit(AllNode node);

    void visit(AllValueNode node);

    void visit(PropertyNode node);

    void visit(ColumnNode node);

    void visit(EachNode node);

    void visit(ExceptNode node);

    void visit(IndexNode node);

    void visit(IndexOfNode node);

    void visit(ListNode node);

    void visit(NullNode node);

    void visit(DirectoryNode node);

    void visit(FileNode node);

    void visit(PathNode node);
}
