package org.ead.generate.data;

import org.ead.utility.ValueCompression;

import java.util.ArrayList;

/**
 * A simple list for numeric values that also supports calculating several compression values for all elements in this list.
 * @author Markus Ullrich
 */
@SuppressWarnings("unused")
public class CompressionList<T extends Number>{

    private ArrayList<T> values;
    private double avg, min, max, total;

    public CompressionList() {
        this.values = new ArrayList<>();
        this.avg = 0;
        this.total = 0;
        this.max = Double.NEGATIVE_INFINITY;
        this.min = Double.POSITIVE_INFINITY;
    }

    /**
     * Adds a value to this list and calculates the new average, min, max total values.
     * @param value the new value
     */
    public void addValue(T value){
        this.values.add(value);
        double doubleValue = value.doubleValue();
        this.total += doubleValue;
        this.avg = this.total / (double) this.values.size();
        if (doubleValue > this.max) {
            this.max = doubleValue;
        }
        if (doubleValue < this.min) {
            this.min = doubleValue;
        }
    }

    public T getValue(int index) {
        return this.values.get(index);
    }

    public double getAvg(){
        return this.avg;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public double getTotal() {
        return this.total;
    }

    public double getMean() {
        double[] doubleValues = new double[this.values.size()];
        for(int i = 0 ; i < doubleValues.length ; i++) {
            doubleValues[i] = this.values.get(i).doubleValue();
        }
        return ValueCompression.getMean(doubleValues);
    }

    public double get25Percentile() {
        double[] doubleValues = new double[this.values.size()];
        for(int i = 0 ; i < doubleValues.length ; i++) {
            doubleValues[i] = this.values.get(i).doubleValue();
        }
        return ValueCompression.get25Percentile(doubleValues);
    }

    public double get75Percentile() {
        double[] doubleValues = new double[this.values.size()];
        for(int i = 0 ; i < doubleValues.length ; i++) {
            doubleValues[i] = this.values.get(i).doubleValue();
        }
        return ValueCompression.get75Percentile(doubleValues);
    }

}
