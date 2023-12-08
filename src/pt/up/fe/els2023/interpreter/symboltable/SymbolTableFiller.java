package pt.up.fe.els2023.interpreter.symboltable;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.export.*;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.racoons.ExporterValue;
import pt.up.fe.els2023.racoons.impl.*;
import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.sources.XmlSource;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTableFiller {

    private final SymbolTable symbolTable;

    private final List<Diagnostic> errors;

    public SymbolTableFiller(EObject root, String racoonsConfigFilename) {
        this.symbolTable = new RacoonsSymbolTable(racoonsConfigFilename);
        this.errors = new ArrayList<>();

        var version = ((RacoonsImpl)root).getVersion();

        symbolTable.addVersion(version.getCode());

        var statements = ((RacoonsImpl)root).getStatements();

        FunctionClassMap<EObject, Void> map = new FunctionClassMap<>();

        map.put(SourceDeclImpl.class, this::fillSourceDecl);
        map.put(NftDeclImpl.class, this::fillNftDecl);
        map.put(ExporterDeclImpl.class, this::fillExporterDecl);
        map.put(ExpressionImpl.class, a -> null);
        map.put(AssignmentImpl.class, a -> null);

        for (var statement : statements) {
            map.apply(statement);
        }

        for (var error : errors) {
            System.out.println(error);
        }
    }

    private Void fillSourceDecl(SourceDeclImpl sourceDecl) {
        var name = sourceDecl.getName();
        var paths = sourceDecl.getPathList();

        if (symbolTable.hasSource(name)) {
            errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), "Source with name '" + name + "' already exists"));
            return null;
        }

        var path = paths.get(0);

        var extension = path.substring(path.lastIndexOf('.') + 1);

        var source = switch (extension) {
            case "yaml" -> new YamlSource(name, paths);
            case "json" -> new JsonSource(name, paths);
            case "xml" ->  new XmlSource(name, paths);
            default -> {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), "Could not determine source type for paths of source '" + name + "'"));
                yield null;
            }
        };

        symbolTable.addSource(name, source);

        return null;
    }

    private Void fillNftDecl(NftDeclImpl nftDecl) {
        System.out.println("NftDecl: " + nftDecl.getName());
        return null;
    }

    private Void fillExporterDecl(ExporterDeclImpl exporterDecl) {
        var name = exporterDecl.getName();
        var type = exporterDecl.getType();
        System.out.println(name);
        System.out.println(type);

        var attrNodes = exporterDecl.getExporterAttrs();

        if (symbolTable.hasExporter(name)) {
            errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), "Exporter with name '" + name + "' already exists"));
            return null;
        }

        var exporterAttrs = new HashMap<String, ExporterValue>();

        for (var attrNode : attrNodes) {
            var attrName = attrNode.getAttr();
            var attrValueNode = attrNode.getValue();

            exporterAttrs.put(attrName, attrValueNode);
        }

        var exporter = switch (type) {
            case "csv" -> {
                var supportedAttrs = CsvExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new CsvExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("separator")) {
                    builder.setSeparator(exporterAttrs.get("separator").getValue());
                }

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "html" -> {
                var supportedAttrs = HtmlExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new HtmlExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("style")) {
                    builder.setStyle(exporterAttrs.get("style").getValue());
                }

                if (exporterAttrs.containsKey("title")) {
                    builder.setTitle(exporterAttrs.get("title").getValue());
                }

                if (exporterAttrs.containsKey("exportFullHtml")) {
                    builder.setExportFullHtml(exporterAttrs.get("exportFullHtml").getValue().equals("true")); // since it's a boolean, only true or false are valid values from the grammar
                }

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "tsv" -> {
                var supportedAttrs = TsvExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new TsvExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "latex" -> {
                var supportedAttrs = LatexExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new LatexExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "markdown" -> {
                var supportedAttrs = MarkdownExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new MarkdownExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        return null;
    }


    // returns true if there are errors
    private boolean checkExporterAttributes(String name,
                                            String exporterType,
                                            Map<String, ExporterValue> exporterAttrs,
                                            Map<String, TableExporter.AttributeValue> supportedAttrs) {
        for (var specifiedAttr : exporterAttrs.keySet()) {
            if (!supportedAttrs.containsKey(specifiedAttr)) {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), NodeModelUtils.getNode(exporterAttrs.get(specifiedAttr)).getStartLine(), -1, exporterType + " Exporter '" + name + "' does not support attribute '" + specifiedAttr + "'"));
            }
        }

        if (!errors.isEmpty()) {
            return true;
        }

        for (var supportedAttr : supportedAttrs.keySet()) {
            if (!exporterAttrs.containsKey(supportedAttr) && supportedAttrs.get(supportedAttr).required()) {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), exporterType + " Exporter '" + name + "' does not have mandatory attribute '" + supportedAttr + "'"));
            }
        }

        if (!errors.isEmpty()) {
            return true;
        }

        for (var specifiedAttr : exporterAttrs.keySet()) {
            var attrValueNode = exporterAttrs.get(specifiedAttr);
            var supportedAttr = supportedAttrs.get(specifiedAttr);

            if (!attrValueNode.eClass().getName().equalsIgnoreCase(supportedAttr.type().toString())) {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), NodeModelUtils.getNode(attrValueNode).getStartLine(), -1, exporterType + " Exporter '" + name + "' attribute '" + specifiedAttr + "' has type '" + attrValueNode.eClass().getName() + "' but should have type '" + StringUtils.capitalize(supportedAttr.type().toString().toLowerCase()) + "'"));
            }
        }

        return !errors.isEmpty();
    }
}
