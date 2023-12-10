package pt.up.fe.els2023.interpreter.syntactic;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import pt.up.fe.els2023.RacoonsStandaloneSetup;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class SyntacticAnalyser implements SyntacticAnalysis {

    public SyntacticAnalyser() {
        var injector = new RacoonsStandaloneSetup().createInjectorAndDoEMFRegistration();
        injector.injectMembers(this);
    }

    @Override
    public SyntacticAnalysisResult analyse(String racoonsConfigFilepath, Map<String, String> config) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(URI.createFileURI(racoonsConfigFilepath));

        // TODO use proper split
        var filename = racoonsConfigFilepath.substring(racoonsConfigFilepath.lastIndexOf('/') + 1);

        var errors = new ArrayList<Diagnostic>();
        var warnings = new ArrayList<Diagnostic>();

        try {
            resource.load(Collections.emptyMap());

            resource.getErrors().forEach(error ->
                    errors.add(Diagnostic.error(filename, error.getLine(), error.getColumn(), error.getMessage())));

            resource.getWarnings().forEach(warning ->
                    warnings.add(Diagnostic.warning(filename, warning.getLine(), warning.getColumn(), warning.getMessage())));
        } catch (IOException e) {
            errors.add(Diagnostic.error(racoonsConfigFilepath, e.getMessage()));
        }

        var treeIterator = resource.getAllContents();
        var rootNode = treeIterator.hasNext()? treeIterator.next() : null;

        return new SyntacticAnalysisResult(filename, rootNode, warnings, errors);
    }
}
