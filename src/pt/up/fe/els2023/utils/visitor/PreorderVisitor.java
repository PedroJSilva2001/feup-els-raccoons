package pt.up.fe.els2023.utils.visitor;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.specs.util.SpecsCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class PreorderVisitor<D, R> extends AllNodesVisitor<D, R> {
    public PreorderVisitor() {

    }

    @Override
    public R visit(EObject node, D data) {
        SpecsCheck.checkNotNull(node, () -> "Node should not be null");

        var visit = getVisit(node.getClass().getName());

        // Preorder: 1st visit the node
        var nodeResult = visit.apply(node, data);

        // Preorder: then, visit each children
        List<R> childrenResults = new ArrayList<>();
        for (var child : node.eContents()) {
            childrenResults.add(visit(child, data));
        }

        var reduceFunction = getReduce();

        // No reduce function, just return result of the node
        if (reduceFunction == null) {
            return nodeResult;
        }

        return reduceFunction.apply(nodeResult, childrenResults);
    }
}
