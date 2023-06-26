package io.gofannon.apl;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationContext {

    private String currentConfigurationName;

    private final List<Parameter> parameterList = new ArrayList<>();

    public String getCurrentConfigurationName() {
        return currentConfigurationName;
    }

    public void setCurrentConfigurationName(String currentConfigurationName) {
        this.currentConfigurationName = currentConfigurationName;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void addParameter( String parameterName, String defaultValue, String description) {
        parameterList.add(new Parameter(
                currentConfigurationName,
                parameterName,
                defaultValue,
                description
        ));
    }
}
