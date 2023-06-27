package io.gofannon.apl;

import java.util.List;

public class ConfigurationParameterMetadataExtractionResult {

    private  List<Parameter> parameterList ;

    public ConfigurationParameterMetadataExtractionResult(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }
}
