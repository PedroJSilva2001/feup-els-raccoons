package pt.up.fe.els2023.operations;

import java.util.List;

public class CompositeOperationBuilder {
    private final String initialTable;
    private String result;
    private final List<TableOperation> operations;
    private String resultVariable;

    public CompositeOperationBuilder(String initialTable, List<TableOperation> operations) {
        this.initialTable = initialTable;
        this.operations = operations;
    }

    public CompositeOperationBuilder setResult(String result) {
        this.result = result;
        return this;
    }

    public CompositeOperationBuilder setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
        return this;
    }

    public CompositeOperation build() {
        return new CompositeOperation(this.initialTable, this.result, this.operations, this.resultVariable);
    }
}
