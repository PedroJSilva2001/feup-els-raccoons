package pt.up.fe.els2023;

import pt.up.fe.els2023.sources.TableSource;

import java.util.List;

public record TableSchema(
        String name,
        List<ColumnSchema> columnSchemas,
        TableSource source
){}
