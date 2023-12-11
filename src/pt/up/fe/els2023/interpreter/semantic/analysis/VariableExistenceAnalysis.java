package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.impl.AssignmentImpl;
import pt.up.fe.els2023.racoons.impl.ExpressionImpl;
import pt.up.fe.els2023.racoons.impl.IdentifierImpl;

// Checks if for a given expression (loose expression or rhs of an assignment) the symbol exists as a
// declared normal variable or other types of declarations (source, nft, exporter)

public class VariableExistenceAnalysis extends VisitorSemanticAnalysis {
    public VariableExistenceAnalysis() {
        super();

        addVisit(AssignmentImpl.class.getName(), this::visitAssignment);
        addVisit(ExpressionImpl.class.getName(), this::visitExpression);
        addVisit(IdentifierImpl.class.getName(), this::visitIdentifier);
    }

    private Void visitIdentifier(EObject node, SymbolTable symbolTable) {
        var id = ((IdentifierImpl) node).getId();

        if (!symbolTable.hasRawSymbol(id)) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Symbol '" + id + "' does not exist"));
        }
        return null;
    }

    private Void visitAssignment(EObject node, SymbolTable symbolTable) {
        var assignment = (AssignmentImpl) node;

        visit(assignment.getRhs(), symbolTable);
        // visit(assignment.getRhs().getExpression(), symbolTable);
        return null;
    }

    private Void visitExpression(EObject node, SymbolTable symbolTable) {
        /*if (node.getClass().getName().equals(IdentifierImpl.class.getName())) {
            visitIdentifier(node, symbolTable);
            return null;
        }*/

        var expression = ((ExpressionImpl) node).getExpression();

        var it = expression.eAllContents();

        while (it.hasNext()) {
            var child = it.next();

            //visitExpression(child, symbolTable);
            visit(child, symbolTable);
        }

        return null;
    }
}
