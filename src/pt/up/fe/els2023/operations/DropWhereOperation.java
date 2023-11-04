package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.Value;

import java.util.Map;

public class DropWhereOperation extends WhereOperation {

    public DropWhereOperation(String predicate) {
        super(predicate);
    }

    public void accept(BTCinterpreter btcInterpreter) {
        btcInterpreter.apply(this);
    }

    public BeginTableCascade execute(BeginTableCascade btc, Map<String, Value> resultVariables) {
        return btc.dropWhere(parsePredicate(predicate, resultVariables));
    }
}
