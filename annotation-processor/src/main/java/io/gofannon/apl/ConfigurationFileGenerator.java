package io.gofannon.apl;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
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
                 PropertiesGenerator propertiesGenerator = new PropertiesGenerator(out)
            ) {

                generateConfigurationFile(propertiesGenerator);

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


    private void generateConfigurationFile(PropertiesGenerator propertiesGenerator) throws IOException {
        for (Parameter parameter : parameterList) {
            propertiesGenerator
                    .comment("Parameter " + parameter.parameter())
                    .comment(parameter.description())
                    .commentedProperty(parameter.parameter(), parameter.defaultValue())
                    .property(parameter.parameter(), parameter.defaultValue())
                    .skipLines(2);
        }
    }
}
