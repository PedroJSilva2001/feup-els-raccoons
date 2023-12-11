package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.impl.IdentifierImpl;

// Checks if for a given expression (loose expression or rhs of an assignment) the symbol exists as a
// declared normal variable or other types of declarations (source, nft, exporter)

public class VariableExistenceAnalysis extends PreorderSemanticAnalysis {
    public VariableExistenceAnalysis() {
        super();

        addVisit(IdentifierImpl.class.getName(), this::visitIdentifier);
    }

    private Void visitIdentifier(EObject node, SymbolTable symbolTable) {
        var id = ((IdentifierImpl) node).getId();

        if (!symbolTable.hasSymbol(id)) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Symbol '" + id + "' does not exist"));
        }
        return null;
    }
}
