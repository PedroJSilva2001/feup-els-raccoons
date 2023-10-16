package pt.up.fe.els2023;

import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.exceptions.NodeTraversalException;

public interface NodeVisitor {
    void visit(AllContainerNode node) throws NodeTraversalException;

    void visit(AllNode node) throws NodeTraversalException;

    void visit(AllValueNode node) throws NodeTraversalException;

    void visit(ChildNode node) throws NodeTraversalException;

    void visit(ColumnNode node);

    void visit(EachNode node) throws NodeTraversalException;

    void visit(ExceptNode node) throws NodeTraversalException;

    void visit(IndexNode node) throws NodeTraversalException;

    void visit(ListNode node) throws NodeTraversalException;

    void visit(NullNode node);
}
