package pt.up.fe.els2023.model.operations;

import java.util.Collections;
import java.util.List;

public abstract class TerminalOperation extends TableOperation {
    private final List<String> columns;

    public TerminalOperation(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    public List<String> getColumns() {
        return Collections.unmodifiableList(columns);
    }
}
