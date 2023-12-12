package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.impl.*;

public class FilterSpecialOperationsUseAnalysis extends VisitorSemanticAnalysis {
    public FilterSpecialOperationsUseAnalysis() {
        super();

        addVisit(AssignmentImpl.class.getName(), this::visitNode);
        addVisit(ExpressionImpl.class.getName(), this::visitNode);
    }

    private Void visitNode(EObject node, SymbolTable symbolTable) {
        var it = node.eAllContents();

        while (it.hasNext()) {
            var expression = it.next();

            if (expression.getClass().getName().equals(OperationCallImpl.class.getName())) {
                if (((OperationCallImpl) expression).getName().equals("where")
                        || ((OperationCallImpl) expression).getName().equals("dropWhere")) {
                    it.prune();
                }
            } else if (expression.getClass().getName().equals(ColumnAccessImpl.class.getName()) ||
                    expression.getClass().getName().equals(PresenceOpImpl.class.getName()) ||
                    expression.getClass().getName().equals(NullCheckImpl.class.getName())) {
                String operationName;
                if (expression.getClass().getName().equals(ColumnAccessImpl.class.getName())) {
                    operationName = "col";
                } else if (expression.getClass().getName().equals(PresenceOpImpl.class.getName())) {
                    operationName = "containsCol";
                } else {
                    operationName = "isNull";
                }
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1,
                        "Prohibited use of function '" + operationName + "' in line " + NodeModelUtils.getNode(node).getStartLine()));
            }
        }

        return null;
    }
}
