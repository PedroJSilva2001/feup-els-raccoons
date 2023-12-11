package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.els2023.interpreter.symboltable.Symbol;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.*;
import pt.up.fe.els2023.racoons.impl.*;


public class AssignedVariableTypeInferAnalysis extends PreorderSemanticAnalysis {
    public AssignedVariableTypeInferAnalysis() {
        super();

        addVisit(AssignmentImpl.class.getName(), this::visitAssignment);
    }

    private Symbol.Type getType(Expression rhs, SymbolTable symbolTable) {
        var rhsType = rhs.getExpression().getClass().getName();

        if (rhsType.equals(LogicalAndImpl.class.getName()) || rhsType.equals(LogicalOrImpl.class.getName()) ||
                rhsType.equals(EqualsAndNEqualsImpl.class.getName()) || rhsType.equals(ComparisonImpl.class.getName())) {
            return Symbol.Type.BOOLEAN;
        } else if (rhsType.equals(AddAndSubImpl.class.getName()) || rhsType.equals(MultAndDivImpl.class.getName())) {
            return Symbol.Type.NUMBER;
        } else if (rhsType.equals(UnaryPreOpImpl.class.getName())) {
            var op = rhs.getExpression().getOp();

            switch (op) {
                case "++":
                case "--":
                    return Symbol.Type.NUMBER;
                case "!":
                    return Symbol.Type.BOOLEAN;
                default:
                    break;
            }
        } else if (rhsType.equals(TableCascadeImpl.class.getName())) {
            var lastOperation = (OperationCallImpl) rhs.getExpression().getRight();

            switch (lastOperation.getName()) {
                case "count", "max", "min", "mean", "std", "var", "sum":
                    if (lastOperation.getParameters().size() > 1) {
                        return Symbol.Type.MAP;
                    } else {
                        return Symbol.Type.NUMBER;
                    }
                default:
                    return Symbol.Type.TABLE;
            }
        } else {
            if (rhsType.equals(ParenthesisImpl.class.getName())) {
                return getType(((ParenthesisImpl) rhs.getExpression()).getElement(), symbolTable);
            } else if (rhsType.equals(ColumnAccessImpl.class.getName())) {
                return null;
            } else if (rhsType.equals(PresenceOpImpl.class.getName())) {
                return null;
            } else if (rhsType.equals(IntLiteralImpl.class.getName())) {
                return Symbol.Type.INTEGER;
            } else if (rhsType.equals(OperationCallImpl.class.getName())) {
                return Symbol.Type.TABLE;
            } else if (rhsType.equals(StringLiteralImpl.class.getName())) {
                return Symbol.Type.STRING;
            } else if (rhsType.equals(DoubleLiteralImpl.class.getName())) {
                return Symbol.Type.DOUBLE;
            } else if (rhsType.equals(BooleanLiteralImpl.class.getName())) {
                return Symbol.Type.BOOLEAN;
            } else if (rhsType.equals(IdentifierImpl.class.getName())) {

                var idName = ((IdentifierImpl) rhs.getExpression()).getId();

                var symbol = symbolTable.getRawSymbol(idName);

                assert symbol != null;

                return symbol.type();
            }
        }

        return null;
    }


    private Void visitAssignment(EObject node, SymbolTable symbolTable) {
        var assignment = (Assignment) node;

        var assignedVariableName = assignment.getName();

        var rhs = assignment.getRhs();

        var type = getType(rhs, symbolTable);

        if (type != null) {
            //System.out.println(assignedVariableName + " is of type " + type.name() + " :) :o :(");
            symbolTable.updateRawSymbolType(assignedVariableName, type);
        }

        return null;
    }
}
