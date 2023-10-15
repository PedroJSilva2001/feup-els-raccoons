package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;

public class BTC {
    private final ITable table;

    public BTC(ITable table) {
        this.table = table;
    }

    public BTC where(Predicate predicate) {
        // isto mal
        ITable newTable = new Table(null,null);


        for (var row : newTable.getRows()) {
            if (predicate.test(row)) {
                // vai para a nova tabela
            }
        }

        return new BTC(newTable);
    }

    public ITable get() {
        return table;
    }

    public Double max(String column) {

        return null;
    }
}
