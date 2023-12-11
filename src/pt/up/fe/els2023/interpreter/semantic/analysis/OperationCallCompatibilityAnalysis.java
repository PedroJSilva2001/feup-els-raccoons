package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.OperationCall;
import pt.up.fe.els2023.racoons.impl.OperationCallImpl;

public class OperationCallCompatibilityAnalysis extends PreorderSemanticAnalysis {
    public OperationCallCompatibilityAnalysis() {
        super();
        addVisit(OperationCallImpl.class.getName(), this::visitOperationCall);
    }

    private Void visitOperationCall(EObject node, SymbolTable symbolTable) {
        var operationCall = (OperationCall) node;
        var operationName = operationCall.getName();

        operationCall.getParameters();

        return null;
    }

}
