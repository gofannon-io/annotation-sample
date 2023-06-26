package io.gofannon.apl;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationVisitResult {

    private  List<Parameter> parameterList ;

    public ConfigurationVisitResult(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }
}
