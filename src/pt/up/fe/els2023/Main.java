package pt.up.fe.els2023;


import pt.up.fe.els2023.interpreter.diagnostic.Reportable;
import pt.up.fe.els2023.interpreter.semantic.SemanticAnalyser;
import pt.up.fe.els2023.interpreter.syntactic.SyntacticAnalyser;

import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar els2023.jar <config_file>");
            return;
        }

        String configFile = args[0];

        System.out.println("Using Racoons config file: " + configFile);


        var syntaxResult = new SyntacticAnalyser().analyse(configFile, Collections.emptyMap());

        if (report(syntaxResult)) {
            System.out.println("Syntax errors found. Aborting.");
            return;
        }

        var semanticResult = new SemanticAnalyser().analyse(syntaxResult, Collections.emptyMap());


        if (report(semanticResult)) {
            System.out.println("Semantic errors found. Aborting.");
            return;
        }
    }

    private static boolean report(Reportable reportable) {
        for (var info : reportable.infos()) {
            System.out.println(info);
        }

        for (var warning : reportable.warnings()) {
            System.out.println(warning);
        }

        if (reportable.errors().isEmpty()) {
            return false;
        }

        for (var error : reportable.errors()) {
            System.out.println(error);
        }

        return true;
    }
}