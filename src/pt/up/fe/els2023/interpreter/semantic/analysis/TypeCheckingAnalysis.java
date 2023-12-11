package pt.up.fe.els2023.interpreter.semantic.analysis;

public class TypeCheckingAnalysis {

    /*private Symbol.Type checkType(Expression node, SymbolTable symbolTable) {
        var expression = node.getExpression();
        var expressionType = expression.getClass().getName();

        if (expressionType.equals(LogicalAndImpl.class.getName()) || expressionType.equals(LogicalOrImpl.class.getName()) ||
                expressionType.equals(EqualsAndNEqualsImpl.class.getName()) || expressionType.equals(ComparisonImpl.class.getName())) {
            var leftSide = checkType((Expression) expression.getLeft(), symbolTable);
            var rightSide = checkType((Expression) expression.getRight(), symbolTable);
            // TODO: Check if they're boolean or not and give an error if not

        } else if (expressionType.equals(AddAndSubImpl.class.getName()) || expressionType.equals(MultAndDivImpl.class.getName())) {
            var leftSide = checkType((Expression) expression.getLeft(), symbolTable);
            var rightSide = checkType((Expression) expression.getRight(), symbolTable);
            // TODO: Check if they're numbers or not and give an error if not

        } else if (expressionType.equals(UnaryPreOpImpl.class.getName())) {
            var op = expression.getOp();
            var subExpression = checkType((Expression) ((UnaryPreOp) expression).getSubExpression(), symbolTable);

            switch (op) {
                case "++":
                case "--":
                    // TODO: Check if they're numbers
                case "!":
                    // TODO: Check if they're boolean
                default:
                    break;
            }
        } else if (expressionType.equals(TableCascadeImpl.class.getName())) {
            var lastOperation = (OperationCallImpl) expression.getRight();

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
            if (expressionType.equals(ParenthesisImpl.class.getName())) {
                return checkType(((ParenthesisImpl) expression).getElement(), symbolTable);
            } else if (expressionType.equals(ColumnAccessImpl.class.getName())) {
                return null;
            } else if (expressionType.equals(PresenceOpImpl.class.getName())) {
                return Symbol.Type.BOOLEAN;
            } else if (expressionType.equals(IntLiteralImpl.class.getName())) {
                return Symbol.Type.INTEGER;
            } else if (expressionType.equals(TableCascadeStartImpl.class.getName())) {
                return Symbol.Type.TABLE;
            } else if (expressionType.equals(StringLiteralImpl.class.getName())) {
                return Symbol.Type.STRING;
            } else if (expressionType.equals(DoubleLiteralImpl.class.getName())) {
                return Symbol.Type.DOUBLE;
            } else if (expressionType.equals(BooleanLiteralImpl.class.getName())) {
                return Symbol.Type.BOOLEAN;
            } else if (expressionType.equals(IdentifierImpl.class.getName())) {

                var idName = ((IdentifierImpl) expression).getId();

                var symbol = symbolTable.getRawSymbol(idName);

                assert symbol != null;

                return symbol.type();
            }
        }

        return null;
    }*/


}
