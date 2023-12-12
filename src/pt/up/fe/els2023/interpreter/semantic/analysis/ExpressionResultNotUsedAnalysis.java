package pt.up.fe.els2023.interpreter.semantic.analysis;

// Checks if the result of an expression is used
// simple expressions (e.g. 1 + 2) need to also be assigned to a variable
// table operations results need to be assigned to a variable with the exception of export()

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.Expression;
import pt.up.fe.els2023.racoons.impl.ExpressionImpl;
import pt.up.fe.els2023.racoons.impl.OperationCallImpl;
import pt.up.fe.els2023.racoons.impl.TableCascadeImpl;

public class ExpressionResultNotUsedAnalysis extends VisitorSemanticAnalysis {
    public ExpressionResultNotUsedAnalysis() {
        super();

        addVisit(ExpressionImpl.class.getName(), this::visitExpression);
    }

    private Void visitExpression(EObject node, SymbolTable symbolTable) {
        var expression = ((Expression) node).getExpression();

        if (!expression.getClass().getName().equals(TableCascadeImpl.class.getName())
                || !((OperationCallImpl) expression.getRight()).getName().equals("export")) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Result from expression in line '" + NodeModelUtils.getNode(node).getStartLine() + "' is never assigned"));
        }

        return null;
    }
}
