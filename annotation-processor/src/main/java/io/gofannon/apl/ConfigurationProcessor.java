package io.gofannon.apl;

import com.google.auto.service.AutoService;
import io.gofannon.apl.annotation.TheConf;
import io.gofannon.apl.annotation.TheConfParam;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


@SupportedAnnotationTypes({
        "io.gofannon.apl.annotation.TheConf",
        "io.gofannon.apl.annotation.TheConfParam"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class ConfigurationProcessor extends AbstractProcessor {

    private List<Parameter> parameterList = new ArrayList<>();

    private boolean errorDetected;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> confElements = roundEnv.getElementsAnnotatedWith(TheConf.class);
        confElements.forEach(confElement -> {
            var isChecked = checkConfigAnnotatedElement(confElement);
            errorDetected = errorDetected && isChecked;
        });

        Set<? extends Element> paramElements = roundEnv.getElementsAnnotatedWith(TheConfParam.class);
        paramElements.forEach(paramElement -> {
            var isChecked = checkConfigParamAnnotatedElement(paramElement);
            errorDetected = errorDetected && isChecked;
        });

        if (errorDetected)
            return false;

        if (!generate(confElements))
            return false;



        return true;
    }

    private boolean checkConfigAnnotatedElement(Element confElement) {
        TheConf annotation = confElement.getAnnotation(TheConf.class);
        if (confElement.getKind() != ElementKind.INTERFACE) {
            String message = "Invalid type " + confElement + ": TheConf annotation shall be located on an interface";
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
            return false;
        }

        if (annotation.name() == null || annotation.name().trim().isEmpty()) {
            String message = "Missing name " + confElement + ": TheConf annotation shall contain a name attribute";
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
            return false;
        }

        String message = "Processing configuration '" + annotation.name() + "' on interface " + confElement;
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
        return true;
    }


    private boolean checkConfigParamAnnotatedElement(Element paramElement) {
        TheConfParam annotation = paramElement.getAnnotation(TheConfParam.class);

        if (paramElement.getKind() != ElementKind.FIELD) {
            String message = "Invalid type " + paramElement + ": TheConfParam annotation shall be located on a field";
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
            return false;
        }

        if (!String.class.getTypeName().equals(paramElement.asType().toString())) {
            String message = "Invalid field type " + paramElement + ": TheConfParam annotation shall be located on a String field";
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
            return false;
        }

        ParameterVisitor visitor = new ParameterVisitor();
        ElementContext context = new ElementContext();
        StringVisitResult result = paramElement.accept(visitor, context);
        System.out.println("Result: " + result);

        return true;
    }

    private boolean generate(Set<? extends Element> confElements) {
        AtomicBoolean error = new AtomicBoolean(false);

        confElements.forEach(confElement -> {
            boolean result = generate(confElement);
            if (!result)
                error.set(true);
        });
        return error.get();
    }

    private boolean generate(Element confElement) {
        ConfigurationVisitor visitor = new ConfigurationVisitor();
        ConfigurationContext context = new ConfigurationContext();
        try {

            ConfigurationVisitResult result = confElement.accept(visitor, context);
            parameterList.addAll(result.getParameterList());
            return true;

        } catch (InvalidParameterNameException ex) {
            return false;
        }
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }
}