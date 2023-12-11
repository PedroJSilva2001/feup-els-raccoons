package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.impl.IdentifierImpl;
import pt.up.fe.els2023.racoons.impl.OperationCallImpl;
import pt.up.fe.els2023.racoons.impl.TableCascadeImpl;

// checks if a table cascade starts with one of the following:
// table() // empty table
// table(<nft>) // table created with nft
// <id> // table identified by id

public class TableCascadeStartAnalysis extends PreorderSemanticAnalysis {
    public TableCascadeStartAnalysis() {
        super();

        addVisit(TableCascadeImpl.class.getName(), this::visitTableCascadeImpl);
    }

    private Void visitTableCascadeImpl(EObject node, SymbolTable symbolTable) {
        var tableCascade = (TableCascadeImpl) node;

        // if this is the last cascade element, before the expressionTerminal
        if (tableCascade.getLeft().getClass().getName().equals(TableCascadeImpl.class.getName())) {
            return null;
        }

        /*var terminalIsBeforeCurrent = tableCascade.getLeft().getLeft() == null;

        if (!terminalIsBeforeCurrent) {
            //visitTableCascadeImpl(tableCascade.getLeft(), symbolTable);
            visit(tableCascade.getLeft(), symbolTable);
            return null;
        }*/

        // terminal reached


        if (tableCascade.getLeft().getClass().getName().equals(IdentifierImpl.class.getName())) {
            return null;
        }

        if (tableCascade.getLeft().getClass().getName().equals(OperationCallImpl.class.getName())) {
            var tableCascadeStart = (OperationCallImpl) tableCascade.getLeft();

            var name = tableCascadeStart.getName();

            if (!name.equals("table")) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1, "Table cascade must begin with table created from constructor table() or table(<nft>) or with an identifier"));
            }

        } else {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1, "Table cascade must begin with table created from constructor table() or table(<nft>) or with an identifier"));
        }

        return null;
    }
}
