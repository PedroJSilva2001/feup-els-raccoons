package pt.up.fe.els2023;

import java.util.List;

public abstract class TableSource {
    String name;
    List<String> files;

    public abstract List<String> getRow(List<String> props);
}
