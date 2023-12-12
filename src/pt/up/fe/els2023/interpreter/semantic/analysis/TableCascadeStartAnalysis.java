package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.Symbol;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.Expression;
import pt.up.fe.els2023.racoons.MapGet;
import pt.up.fe.els2023.racoons.Parenthesis;
import pt.up.fe.els2023.racoons.impl.*;

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

        //System.out.println("visit table cascade impl " + tableCascade);

        // if there is still a left node that is a table cascade, then we are not at the start of the cascade
        if (tableCascade.getLeft().getClass().getName().equals(TableCascadeImpl.class.getName())) {
            return null;
        }

        if (tableCascade.getLeft().getClass().getName().equals(ParenthesisImpl.class.getName())) {
            var isTable = parenthesisExprTypeIsTable(((Parenthesis) tableCascade.getLeft()).getElement(), symbolTable);

            if (isTable == null) {
                // symbol not found. error caught in another analyser (variable existence)
                return null;
            }

            if (!isTable) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1, "Table cascade must begin with table created from either:\n- constructor table() or table(<nft>)\n- variable whose value is a table\n- table cascade without ending in aggregation function (count, max, min, mean, std, var, sum)"));
            }
        }

        if (tableCascade.getLeft().getClass().getName().equals(IdentifierImpl.class.getName())) {
            var idName = ((IdentifierImpl) tableCascade.getLeft()).getId();

            var symbol = symbolTable.getRawSymbol(idName);

            if (symbol == null) {
                // symbol not found. error caught in another analyser (variable existence)
                return null;
            }

            if (!symbol.type().equals(Symbol.Type.TABLE)) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1, "Table cascade started with variable '" + idName +
                                "' whose value is not a table. Value of '" + idName + "' is " +
                                StringUtils.capitalize(symbol.type().toString().toLowerCase())));
                return null;
            }
        }

        if (tableCascade.getLeft().getClass().getName().equals(OperationCallImpl.class.getName())) {
            var tableCascadeStart = (OperationCallImpl) tableCascade.getLeft();

            var name = tableCascadeStart.getName();

            // todo think about cascades started with expressions (table1 -> where()) -> max()
            // todo check identifier being a table
            if (!name.equals("table")) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1, "Table cascade started with operation " + name + "(). Table cascades must begin with table created from either:\n- constructor table([<nft>])\n- variable whose value is a table\n- table cascade expression that doesn't end in aggregation function (count, max, min, mean, std, var, sum)"));
                return null;
            }

        }/* else {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1, "Table cascade must begin with table created from constructor table() or table(<nft>) or with an identifier"));
        }*/

        return null;
    }


    private Boolean parenthesisExprTypeIsTable(Expression rhs, SymbolTable symbolTable) {
        var rhsType = rhs.getExpression().getClass().getName();

        if (rhsType.equals(ParenthesisImpl.class.getName())) {
            return parenthesisExprTypeIsTable(((ParenthesisImpl) rhs.getExpression()).getElement(), symbolTable);
        }

        if (rhsType.equals(TableCascadeImpl.class.getName())) {
            var lastOperation = (OperationCallImpl) rhs.getExpression().getRight();

            switch (lastOperation.getName()) {
                case "count", "max", "min", "mean", "std", "var", "sum":
                    if (lastOperation.getParameters().size() > 1) {
                        return false;
                    } else {
                        return false;
                    }
                default:
                    return true;
            }
        }

        if (rhsType.equals(OperationCallImpl.class.getName())) {
            var name = ((OperationCallImpl) rhs.getExpression()).getName();

            if (!name.equals("table")) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(rhs).getStartLine(), -1, "Table cascade started with operation " + name + "(). Table cascades must begin with table created from either:\n- constructor table([<nft>])\n- variable whose value is a table\n- table cascade expression that doesn't end in aggregation function (count, max, min, mean, std, var, sum)"));
                return null;
            }

        }

        if (rhsType.equals(IdentifierImpl.class.getName())) {
            var idName = ((IdentifierImpl) rhs.getExpression()).getId();

            var symbol = symbolTable.getRawSymbol(idName);

            if (symbol == null) {
                // symbol not found. error caught in another analyser (variable existence)
                return null;
            }

            return symbol.type().equals(Symbol.Type.TABLE);
        }

        return false;
    }

}
