package pt.up.fe.els2023;

import pt.up.fe.els2023.config.Config;
import pt.up.fe.els2023.exceptions.TableNotFoundException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar els2023.jar <config_file>");
            return;
        }

        String configFile = args[0];

        ConfigReader configReader = new ConfigReader(configFile);
        try {
            Config config = configReader.readConfig();

            Interpreter interpreter = new Interpreter();
            var tables = interpreter.buildTables(config);

            var operations = config.operations();
            for (var pipeline : operations) {
                var btcInterpreter = pipeline.updateBTC(tables);
                var resultingTable = btcInterpreter.getBtc().get();
                tables.put(pipeline.getResult(), resultingTable);
            }

            config.exporters().forEach((exporter) -> {
                try {
                    exporter.export(tables);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TableNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}