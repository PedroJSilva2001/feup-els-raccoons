package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.OperationCall;
import pt.up.fe.els2023.racoons.impl.*;

import java.util.List;

public class ImproperTerminalOperationAnalysis extends PreorderSemanticAnalysis {
    public ImproperTerminalOperationAnalysis() {
        addVisit(TableCascadeImpl.class.getName(), this::visitTableCascade);
    }


    private Void visitTableCascade(EObject node, SymbolTable symbolTable) {
        var tableCascade = (TableCascadeImpl) node;

        while (tableCascade.getLeft().getClass().getName().equals(TableCascadeImpl.class.getName())) {
            tableCascade = (TableCascadeImpl) tableCascade.getLeft();

            var operationName = ((OperationCall)tableCascade.getRight()).getName();

            if (operationName.equals("table")) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1,
                        "Table creation operator table() used after start of table cascade. Can only use table() to begin a table cascade."));
            }

            var terminals = List.of(
                    "count",
                    "min",
                    "max",
                    "sum",
                    "mean",
                    "std",
                    "var",
                    "export"
            );

            if (terminals.contains(operationName)) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1,
                        "Table cascade is being consumed with terminal operation " + operationName + "() but still continues with more operations."));
            }

            return null;
        }

        return null;
    }

}
