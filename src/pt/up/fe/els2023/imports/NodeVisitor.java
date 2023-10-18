package pt.up.fe.els2023.imports;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.exceptions.NodeTraversalException;

public interface NodeVisitor {
    void visit(AllContainerNode node);

    void visit(AllNode node);

    void visit(AllValueNode node);

    void visit(ChildNode node);

    void visit(ColumnNode node);

    void visit(EachNode node);

    void visit(ExceptNode node);

    void visit(IndexNode node);

    void visit(ListNode node);

    void visit(NullNode node);
}
