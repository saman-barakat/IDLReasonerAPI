package idlreasonerchoco.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationAnalysisResponse {

    private Boolean valid;
    private Boolean deadParameter;
    private Boolean consistent;
    private Boolean falseOptional;

    private Map<String, Map<String, List<String>>> analysisResult;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getDeadParameter() {
        return deadParameter;
    }

    public void setDeadParameter(Boolean deadParameter) {
        this.deadParameter = deadParameter;
    }

    public Boolean getConsistent() {
        return consistent;
    }

    public void setConsistent(Boolean consistent) {
        this.consistent = consistent;
    }

    public Boolean getFalseOptional() {
        return falseOptional;
    }

    public void setFalseOptional(Boolean falseOptional) {
        this.falseOptional = falseOptional;
    }

    public Map<String, Map<String, List<String>>> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(Map<String, Map<String, List<String>>> analysisResult) {
        this.analysisResult = analysisResult;
    }
}
