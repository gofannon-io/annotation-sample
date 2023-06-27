package io.gofannon.apl;

import io.gofannon.apl.annotation.TheConf;
import io.gofannon.apl.annotation.TheConfParam;

import javax.lang.model.element.*;

public class ConfigurationVisitor implements ElementVisitor<ConfigurationParameterMetadataExtractionResult, ConfigurationContext> {


    @Override
    public ConfigurationParameterMetadataExtractionResult visit(Element ge, ConfigurationContext configurationContext) {
        if (ge instanceof VariableElement) {
            VariableElement e = (VariableElement) ge;
            TheConfParam annotation = e.getAnnotation(TheConfParam.class);
            if (annotation != null) {
                String defaultValue = annotation.defaultValue();
                String description = annotation.description();
                Object constValueObj = e.getConstantValue();
                String paramName = constValueObj == null ? "" : constValueObj.toString();
                if (!paramName.startsWith(configurationContext.getCurrentConfigurationName() + ".")) {
                    String message = "In @TheConf " + configurationContext.getCurrentConfigurationName() + ", @Parameter " + e.getSimpleName() + " has invalid name";
                    throw new InvalidParameterNameException(message);
                }

                configurationContext.addParameter(paramName, defaultValue, description);
            }
        }

        return null;

//        TheConf annotation = e.getAnnotation(TheConf.class);
//        if (annotation != null) {
//            configurationContext.setCurrentConfigurationName(annotation.name());
//        }
//        return new ConfigurationVisitResult(configurationContext.getParameterList());
    }

    @Override
    public ConfigurationParameterMetadataExtractionResult visitPackage(PackageElement e, ConfigurationContext configurationContext) {
        return null;
    }

    @Override
    public ConfigurationParameterMetadataExtractionResult visitType(TypeElement e, ConfigurationContext configurationContext) {
        TheConf annotation = e.getAnnotation(TheConf.class);
        if (annotation != null) {
            configurationContext.setCurrentConfigurationName(annotation.name());
            e.getEnclosedElements().forEach(ee -> {
                if (ee instanceof VariableElement)
                    this.visit((VariableElement) ee, configurationContext);
            });
        }
        return new ConfigurationParameterMetadataExtractionResult(configurationContext.getParameterList());
    }

    @Override
    public ConfigurationParameterMetadataExtractionResult visitVariable(VariableElement e, ConfigurationContext configurationContext) {
        TheConfParam annotation = e.getAnnotation(TheConfParam.class);
        if (annotation != null) {
            String defaultValue = annotation.defaultValue();
            String description = annotation.description();
            Object constValueObj = e.getConstantValue();
            String paramName = constValueObj == null ? "" : constValueObj.toString();
            if (!paramName.startsWith(configurationContext.getCurrentConfigurationName() + ".")) {
                String message = "In @TheConf " + configurationContext.getCurrentConfigurationName() + ", @Parameter " + e.getSimpleName() + " has invalid name";
                throw new InvalidParameterNameException(message);
            }

            configurationContext.addParameter(paramName, defaultValue, description);
        }
        return null;
    }

    @Override
    public ConfigurationParameterMetadataExtractionResult visitExecutable(ExecutableElement e, ConfigurationContext configurationContext) {
        return null;
    }

    @Override
    public ConfigurationParameterMetadataExtractionResult visitTypeParameter(TypeParameterElement e, ConfigurationContext configurationContext) {
        return null;
    }

    @Override
    public ConfigurationParameterMetadataExtractionResult visitUnknown(Element e, ConfigurationContext configurationContext) {
        return null;
    }
}
