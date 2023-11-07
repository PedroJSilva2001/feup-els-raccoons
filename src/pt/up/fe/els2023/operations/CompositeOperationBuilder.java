package pt.up.fe.els2023.operations;

import java.util.List;

public class CompositeOperationBuilder {
    private final String initialTable;
    private String resultVariableName;
    private final List<TableOperation> operations;

    public CompositeOperationBuilder(String initialTable, List<TableOperation> operations) {
        this.initialTable = initialTable;
        this.operations = operations;
    }

    public CompositeOperationBuilder setResultVariableName(String resultVariableName) {
        this.resultVariableName = resultVariableName;
        return this;
    }

    public CompositeOperation build() {
        return new CompositeOperation(this.initialTable, this.resultVariableName, this.operations);
    }
}
