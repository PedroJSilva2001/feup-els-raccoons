package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.signatures.Signatures;
import pt.up.fe.els2023.interpreter.symboltable.Symbol;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.Expression;
import pt.up.fe.els2023.racoons.MapGet;
import pt.up.fe.els2023.racoons.OperationCall;
import pt.up.fe.els2023.racoons.impl.*;

public class OperationCallCompatibilityAnalysis extends PreorderSemanticAnalysis {
    public OperationCallCompatibilityAnalysis() {
        super();

        addVisit(MapGetImpl.class.getName(), this::visitMapGet);
        addVisit(OperationCallImpl.class.getName(), this::visitOperationCall);
    }

    private Void visitOperationCall(EObject node, SymbolTable symbolTable) {
        var operationCall = (OperationCall) node;
        var operationName = operationCall.getName();

        switch (operationName) {
            case "count", "min", "max", "sum", "mean", "std", "var",
                    "select", "reject":
                // variadic string column
                //TODO
                validateVariadicStringColumn(operationName, operationCall, symbolTable);
                break;
            case "where", "dropWhere":
                // predicate only (expression)
                //TODO
                validateFilters(operationName, operationCall, symbolTable);
                break;
            // variadic string column (add this to with aggregates case)
            case "argMax", "argMin", "sort":
                // only one string (column)
                validateNstringsColumn(operationName, operationCall, symbolTable, 1);
                break;
            case "export":
                // exporter
                validateExporter(operationCall, symbolTable);
                break;
            case "table":
                // nft or no args
                validateTableConstructor(operationCall, symbolTable);
                break;
            case "renameColumn":
                // two strings (columns)
                validateNstringsColumn(operationName, operationCall, symbolTable, 2);
                break;
            case "concatHorizontal", "concatVertical":
                //TODO
                validateVariadicTable(operationName, operationCall, symbolTable);
                // variadic table
                break;
            case "join":
                // one table, one string (column)
                validateJoin(operationCall, symbolTable);
                break;
            case "groupBy", "limit":
                break;
            default:
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(node).getStartLine(), -1,
                        "Unknown operation " + operationName + "()"));
                return null;
        }

        return null;
    }

    private void validateJoin(OperationCall operationCall, SymbolTable symbolTable) {
        var operationParams = operationCall.getParameters();
        if (operationParams == null || operationParams.size() != 2 ||
                (!operationParams.get(1).getExpression().getClass().getName().equals(IdentifierImpl.class.getName()) &&
                        !operationParams.get(1).getExpression().getClass().getName().equals(StringLiteralImpl.class.getName()))) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Operation join() must have exactly one argument of type table schema and one argument of type string"));
        } else if (!operationParams.get(0).getExpression().getClass().getName().equals(IdentifierImpl.class.getName()) ||
                !symbolTable.hasTableSchema(((IdentifierImpl) operationParams.get(0).getExpression()).getId()) ||
                symbolTable.getTableSchema(((IdentifierImpl) operationParams.get(0).getExpression()).getId()).type() != Symbol.Type.TABLE_SCHEMA) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationParams.get(0)).getStartLine(), -1,
                    "Symbol '" + ((IdentifierImpl) operationParams.get(0).getExpression()).getId() + "' must be of type table schema"));
        } else if (operationParams.get(1).getExpression().getClass().getName().equals(IdentifierImpl.class.getName()) && (
                !symbolTable.hasRawSymbol(((IdentifierImpl) operationParams.get(1).getExpression()).getId()) ||
                        symbolTable.getRawSymbol(((IdentifierImpl) operationParams.get(1).getExpression()).getId()).type() != Symbol.Type.STRING
        )) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Symbol '" + ((IdentifierImpl) operationParams.get(1).getExpression()).getId() + "' must be of type string"));
        }
    }

    private void validateVariadicTable(String opName, OperationCall operationCall, SymbolTable symbolTable) {
    }

    private void validateTableConstructor(OperationCall operationCall, SymbolTable symbolTable) {
        var operationParams = operationCall.getParameters();
        if (operationParams == null || operationParams.size() != 1
                || !operationParams.get(0).getExpression().getClass().getName().equals(IdentifierImpl.class.getName())) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Operation table() must have one argument of type table schema"));
        } else if (!symbolTable.hasTableSchema(((IdentifierImpl) operationParams.get(0).getExpression()).getId()) ||
                symbolTable.getTableSchema(((IdentifierImpl) operationParams.get(0).getExpression()).getId()).type() != Symbol.Type.TABLE_SCHEMA) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Symbol '" + ((IdentifierImpl) operationParams.get(0).getExpression()).getId() + "' has to be of type table schema"));
        }
    }

    private void validateExporter(OperationCall operationCall, SymbolTable symbolTable) {
        var operationParams = operationCall.getParameters();
        if (operationParams == null || operationParams.size() != 1
                || !operationParams.get(0).getExpression().getClass().getName().equals(IdentifierImpl.class.getName())) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Operation export() must have one argument of type exporter"));
        } else if (!symbolTable.hasExporter(((IdentifierImpl) operationParams.get(0).getExpression()).getId()) ||
                symbolTable.getExporter(((IdentifierImpl) operationParams.get(0).getExpression()).getId()).type() != Symbol.Type.EXPORTER) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Symbol '" + ((IdentifierImpl) operationParams.get(0).getExpression()).getId() + "' has to be of type exporter"));
        }
    }

    private void validateNstringsColumn(String opName, OperationCall operationCall, SymbolTable symbolTable, int i) {
        var operationParams = operationCall.getParameters();
        if (operationParams == null || operationParams.size() != i) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Operation " + opName + "() must have " + i + " argument(s)"));
            return;
        }
        for (var param : operationParams) {
            if (!param.getExpression().getClass().getName().equals(IdentifierImpl.class.getName()) ||
                    !symbolTable.hasRawSymbol(((IdentifierImpl) param.getExpression()).getId()) ||
                    symbolTable.getRawSymbol(((IdentifierImpl) param.getExpression()).getId()).type() != Symbol.Type.STRING) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                        "Symbol '" + ((IdentifierImpl) param.getExpression()).getId() + "' must be of type string"));
            } else if (!param.getExpression().getClass().getName().equals(StringLiteralImpl.class.getName())) {
                addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                        "Operation " + opName + "() must have " + i + " argument(s) of type string"));
            }
        }
    }

    private void validateFilters(String opName, OperationCall operationCall, SymbolTable symbolTable) {
    }

    private void validateVariadicStringColumn(String opName, OperationCall operationCall, SymbolTable symbolTable) {
        var operationParams = operationCall.getParameters();
        if (operationParams.isEmpty()) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(operationCall).getStartLine(), -1,
                    "Operation " + opName + "() must have at least one argument"));
            return;
        }

        // todo validate only strings (StringLiteralImpl)
    }

    private Void visitMapGet(EObject node, SymbolTable symbolTable) {
        var mapGet = (MapGet) node;

        if (mapGet.getExpressions().size() != 2) {
            addError(null);
            return null;
        }

        var firstArg = mapGet.getExpressions().get(0).getExpression();
        var secondArg = mapGet.getExpressions().get(1).getExpression();

        if (!firstArg.getClass().getName().equals(IdentifierImpl.class.getName())) {
            System.out.println("not identifier");
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "First argument of get() must be an identifier to a map"));

            return null;
        }

        System.out.println(secondArg.getClass().getName());
        if (!secondArg.getClass().getName().equals(StringLiteralImpl.class.getName())) {
            System.out.println("not string");
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Second argument of get() must be a string with the name of the column"));
            return null;
        }

        var mapName = ((IdentifierImpl) firstArg).getId();

        var symbol = symbolTable.getRawSymbol(mapName);

        if (symbol == null) { // symbol doesnt exist. already addressed in other analysis
            return null;
        }

        if (symbol.type() != Symbol.Type.MAP) {
            System.out.println("here2");
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "First argument of get() must be an identifier to a map"));
            return null;
        }

        return null;
    }
}
