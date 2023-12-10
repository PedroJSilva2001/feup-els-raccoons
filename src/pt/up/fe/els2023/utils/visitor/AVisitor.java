package pt.up.fe.els2023.utils.visitor;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.specs.util.SpecsCheck;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AVisitor<D, R> implements Visitor<D, R> {
    private final Map<String, BiFunction<EObject, D, R>> visitMap;
    private BiFunction<EObject, D, R> defaultVisit;

    public AVisitor(Map<String, BiFunction<EObject, D, R>> visitMap, BiFunction<EObject, D, R> defaultVisit) {
        this.visitMap = visitMap;
        this.defaultVisit = defaultVisit;
    }

    public AVisitor() {
        this(new HashMap<>(), null);
        setDefaultVisit(this::defaultVisit);
    }

    private R defaultVisit(EObject node, D data) {
        return null;
    }

    @Override
    public void addVisit(String kind, BiFunction<EObject, D, R> method) {
        this.visitMap.put(kind, method);
    }

    @Override
    public void setDefaultVisit(BiFunction<EObject, D, R> defaultVisit) {
        this.defaultVisit = defaultVisit;
    }

    protected BiFunction<EObject, D, R> getVisit(String kind) {
        var visitMethod = visitMap.get(kind);

        if (visitMethod == null) {
            SpecsCheck.checkNotNull(defaultVisit,
                    () -> "No default visitor is set, could not visit node of kind " + kind);

            visitMethod = defaultVisit;
        }

        return visitMethod;
    }

    @Override
    public R visit(EObject node, D data) {
        SpecsCheck.checkNotNull(node, () -> "Node should not be null");

        return getVisit(node.getClass().getName()).apply(node, data);
    }

    protected R visitAllChildren(EObject node, D data) {
        for (var child : node.eContents()) {
            visit(child, data);
        }

        return null;
    }
}
