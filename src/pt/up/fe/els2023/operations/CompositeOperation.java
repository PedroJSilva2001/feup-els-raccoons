package pt.up.fe.els2023.operations;

import java.util.List;

public record CompositeOperation(String initialTable, String result, List<TableOperation> operations,
                                 String resultVariable) {
}
