package pt.up.fe.els2023.interpreter.runtime;

import pt.up.fe.els2023.model.operations.RowWrapper;
import pt.up.fe.els2023.model.table.Value;

public interface RowWrapperFunction {
    Value apply(RowWrapper rowWrapper);
}
