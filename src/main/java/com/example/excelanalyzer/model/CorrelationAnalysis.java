
package com.example.excelanalyzer.model;

import java.util.Map;

public class CorrelationAnalysis {
    private Map<String, Map<String, Double>> correlationMatrix;
    private Map<String, Double> significanceMatrix;
    private String analysisMethod; // PEARSON, SPEARMAN, KENDALL
    private String interpretation;
    private Map<String, String> strongCorrelations;
    
    // Constructors
    public CorrelationAnalysis() {}
    
    public CorrelationAnalysis(String analysisMethod) {
        this.analysisMethod = analysisMethod;
    }
    
    // Getters and Setters
    public Map<String, Map<String, Double>> getCorrelationMatrix() { return correlationMatrix; }
    public void setCorrelationMatrix(Map<String, Map<String, Double>> correlationMatrix) { this.correlationMatrix = correlationMatrix; }
    
    public Map<String, Double> getSignificanceMatrix() { return significanceMatrix; }
    public void setSignificanceMatrix(Map<String, Double> significanceMatrix) { this.significanceMatrix = significanceMatrix; }
    
    public String getAnalysisMethod() { return analysisMethod; }
    public void setAnalysisMethod(String analysisMethod) { this.analysisMethod = analysisMethod; }
    
    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
    
    public Map<String, String> getStrongCorrelations() { return strongCorrelations; }
    public void setStrongCorrelations(Map<String, String> strongCorrelations) { this.strongCorrelations = strongCorrelations; }
}
