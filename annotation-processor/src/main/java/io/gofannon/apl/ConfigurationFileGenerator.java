package io.gofannon.apl;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

class ConfigurationFileGenerator {
    private final ProcessingEnvironment processingEnv;
    private final List<Parameter> parameterList;

    public ConfigurationFileGenerator(ProcessingEnvironment processingEnv, List<Parameter> parameterList) {
        this.processingEnv = processingEnv;
        this.parameterList = parameterList;
    }

    boolean generateConfigurationFile() {
        try {

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating configuration file");


            Filer filer = processingEnv.getFiler();
            FileObject builderFile = filer.createResource(StandardLocation.CLASS_OUTPUT,
                    "",
                    "META-INF/configuration/the-configuration.properties");

            try (OutputStream out = builderFile.openOutputStream();
                 PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {

                generateConfigurationFile(writer);

            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
                return false;
            }

            return true;

        } catch (IOException ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            return false;
        }
    }

    private void generateConfigurationFile(PrintWriter printer) throws IOException {
        for (Parameter parameter : parameterList) {
            printer.println("# Parameter " + parameter.parameter());
            String[] descriptionLines = parameter.description().replaceAll("\r", "").split("\n");
            for (String descriptionLine : descriptionLines) {
                printer.println("# " + descriptionLine);
            }
            printer.println("# " + parameter.parameter() + " = " + parameter.defaultValue());
            printer.println(parameter.parameter() + " = " + parameter.defaultValue());
            printer.println();
            printer.println();
        }
    }

}
