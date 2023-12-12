package pt.up.fe.els2023.utils.visitor;

import org.eclipse.emf.ecore.EObject;

import java.util.function.BiFunction;

public interface Visitor<D, R> {
    R visit(EObject node, D data);

    default R visit(EObject node) {
        return visit(node, null);
    }

    void addVisit(String kind, BiFunction<EObject, D, R> method);

    default void addVisit(EObject node, BiFunction<EObject, D, R> method) {
        addVisit(node.getClass().getName(), method);
    }

    void setDefaultVisit(BiFunction<EObject, D, R> method);
}
