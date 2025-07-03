
package com.example.excelanalyzer.model;

public class ColumnStatistics {
    private String type; // "numeric" or "text"
    private int count;
    private double average;
    private double max;
    private double min;
    private int uniqueValues;

    // Constructors
    public ColumnStatistics() {}

    public ColumnStatistics(String type, int count) {
        this.type = type;
        this.count = count;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public int getUniqueValues() {
        return uniqueValues;
    }

    public void setUniqueValues(int uniqueValues) {
        this.uniqueValues = uniqueValues;
    }
}
