package pt.up.fe.els2023.utils.visitor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AllNodesVisitor<D, R> extends AVisitor<D, R> {
    private BiFunction<R, List<R>, R> reduce;

    public AllNodesVisitor() {
        this.reduce = null;
    }

    public void setReduceSimple(BiFunction<R, R, R> reduce) {
        setReduce(buildReduce(reduce));
    }

    private BiFunction<R, List<R>, R> buildReduce(BiFunction<R, R, R> simpleReduce) {

        return (nodeResult, childrenResults) -> {
            // If no children, simply return node result
            if (childrenResults.isEmpty()) {
                return nodeResult;
            }

            // Merge each children results
            R currentResult = childrenResults.get(0);

            for (int i = 1; i < childrenResults.size(); i++) {
                currentResult = simpleReduce.apply(currentResult, childrenResults.get(i));
            }

            // Merge with node result
            return simpleReduce.apply(currentResult, nodeResult);
        };

    }

    public void setReduce(BiFunction<R, List<R>, R> reduce) {
        this.reduce = reduce;
    }

    protected BiFunction<R, List<R>, R> getReduce() {
        return reduce;
    }

    public void setDefaultValue(Supplier<R> defaultValue) {
        // When setting a default value, the default visit simple returns the default value
        setDefaultVisit((node, data) -> defaultValue.get());
    }
}
