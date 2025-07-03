
package com.example.excelanalyzer.model;

import java.util.List;
import java.util.Map;

public class RegressionAnalysis {
    private String regressionType; // LINEAR, POLYNOMIAL, LOGISTIC
    private String dependentVariable;
    private List<String> independentVariables;
    private Map<String, Double> coefficients;
    private Double rSquared;
    private Double adjustedRSquared;
    private Double fStatistic;
    private Double pValue;
    private String equation;
    private List<Double> residuals;
    private Map<String, Double> variableSignificance;
    private String modelSummary;
    private List<String> assumptions;
    private String interpretation;
    
    // Constructors
    public RegressionAnalysis() {}
    
    public RegressionAnalysis(String regressionType, String dependentVariable) {
        this.regressionType = regressionType;
        this.dependentVariable = dependentVariable;
    }
    
    // Getters and Setters
    public String getRegressionType() { return regressionType; }
    public void setRegressionType(String regressionType) { this.regressionType = regressionType; }
    
    public String getDependentVariable() { return dependentVariable; }
    public void setDependentVariable(String dependentVariable) { this.dependentVariable = dependentVariable; }
    
    public List<String> getIndependentVariables() { return independentVariables; }
    public void setIndependentVariables(List<String> independentVariables) { this.independentVariables = independentVariables; }
    
    public Map<String, Double> getCoefficients() { return coefficients; }
    public void setCoefficients(Map<String, Double> coefficients) { this.coefficients = coefficients; }
    
    public Double getRSquared() { return rSquared; }
    public void setRSquared(Double rSquared) { this.rSquared = rSquared; }
    
    public Double getAdjustedRSquared() { return adjustedRSquared; }
    public void setAdjustedRSquared(Double adjustedRSquared) { this.adjustedRSquared = adjustedRSquared; }
    
    public Double getFStatistic() { return fStatistic; }
    public void setFStatistic(Double fStatistic) { this.fStatistic = fStatistic; }
    
    public Double getPValue() { return pValue; }
    public void setPValue(Double pValue) { this.pValue = pValue; }
    
    public String getEquation() { return equation; }
    public void setEquation(String equation) { this.equation = equation; }
    
    public List<Double> getResiduals() { return residuals; }
    public void setResiduals(List<Double> residuals) { this.residuals = residuals; }
    
    public Map<String, Double> getVariableSignificance() { return variableSignificance; }
    public void setVariableSignificance(Map<String, Double> variableSignificance) { this.variableSignificance = variableSignificance; }
    
    public String getModelSummary() { return modelSummary; }
    public void setModelSummary(String modelSummary) { this.modelSummary = modelSummary; }
    
    public List<String> getAssumptions() { return assumptions; }
    public void setAssumptions(List<String> assumptions) { this.assumptions = assumptions; }
    
    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
}
