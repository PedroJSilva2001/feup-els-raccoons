package pt.up.fe.els2023;

import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.TableCascadeAlreadyConsumedException;
import pt.up.fe.els2023.table.schema.TableSchema;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.export.HtmlExporterBuilder;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.sources.XmlSource;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.els2023.table.Table;

import java.io.IOException;
import java.util.List;

import static pt.up.fe.els2023.dsl.SchemaNodeFactory.all;
import static pt.up.fe.els2023.dsl.SchemaNodeFactory.allValue;
import static pt.up.fe.els2023.dsl.SchemaNodeFactory.directory;
import static pt.up.fe.els2023.dsl.SchemaNodeFactory.each;
import static pt.up.fe.els2023.dsl.SchemaNodeFactory.property;


public class ExampleTest {

    @Test
    public void exampleTest() throws ColumnNotFoundException, TableNotFoundException, IOException, TableCascadeAlreadyConsumedException {
        var decisionTreeSource = new YamlSource(
                "decision_tree",
                List.of(
                        "./test/pt/up/fe/els2023/files/check2/run*/decision_tree.yaml"
                )
        );

        var profilingSource = new JsonSource(
                "profiling",
                List.of(
                        "./test/pt/up/fe/els2023/files/check2/run*/profiling.json"
                )
        );

        var vitisSource = new XmlSource(
                "vitis",
                List.of(
                        "./test/pt/up/fe/els2023/files/check2/run*/vitis-report.xml"
                )
        );

        var vitisResourcesTable = new TableSchema("vitis-resources")
                .source(vitisSource)
                .nft(
                        directory(),
                        property("AreaEstimates",
                                property("Resources",
                                        all()
                                )
                        )
                )
                .collect();

        var decisionTreeParamsTable = new TableSchema("decision-tree-params")
                .source(decisionTreeSource)
                .nft(
                        allValue(),
                        property("params",
                                allValue()
                        )
                )
                .collect();

        var profilingFunctionsTable = new TableSchema("profiling-functions")
                .source(profilingSource)
                .nft(
                        property("functions", each(
                                property("name", "Function Name"),
                                property("time%", "Function Time Percentage")
                        ))
                )
                .collect();

/*        Table finalTable = vitisResourcesTable.btc().concatHorizontal(
                decisionTreeParamsTable,
                profilingFunctionsTable.btc().argMax("Function Time Percentage").get()
        ).get();

        TableExporter exporter = new HtmlExporterBuilder("output.html", "./test/pt/up/fe/els2023/files/check2/out/")
                .setTitle("Vitis Report")
                .setExportFullHtml(true)
                .build();

        exporter.export(finalTable);*/
    }
}