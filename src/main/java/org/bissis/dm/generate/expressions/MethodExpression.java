package org.bissis.dm.generate.expressions;

import org.bissis.dm.generate.data.ProblemData;
import org.bissis.dm.generate.types.NumericDataType;
import org.bissis.dm.utility.ValueCompression;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an expression using any method (formula?)
 * for example statistical information
 * (min, max, avg, sum)
 * Only one identifier (dataTypeName)
 * can be used for this  expression.
 * @author Markus Ullrich
 */
public class MethodExpression implements IExpression {


    private String dataTypeName;
    private String expression;

    private List<Double> valueList;

    public MethodExpression(String dataTypeName, String expression) {
        this.dataTypeName = dataTypeName;
        this.expression = expression;
        this.valueList = new ArrayList<>();
    }

    @Override
    public String evaluate(String value, String secondValue) {
        this.valueList.add(Double.parseDouble(value));
        double[] doubleValues = new double[this.valueList.size()];
        double sum = 0.0;
        for(int i = 0 ; i < doubleValues.length ; i++) {
            doubleValues[i] = this.valueList.get(i);
            sum += doubleValues[i];
        }
        switch (expression) {
            case ("sum"): {
                return "" + sum;
            }
            case ("avg"): {
                return "" + (sum / doubleValues.length);
            }
            case ("min"): {
                return "" + ValueCompression.getMin(doubleValues);
            }
            case ("max"): {
                return "" + ValueCompression.getMax(doubleValues);
            }
            case ("abs"): {
                return "" + Math.abs(Double.parseDouble(value));
            }
        }
        return value;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public String getDataTypeName() {
        return this.dataTypeName;
    }

    @Override
    public String getSecondDataTypeName() {
        return null;
    }

    @Override
    public double getMinValue(ProblemData problemData) {
        double lowerBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getNextLowerBound();
        double upperBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getNextUpperBound();
        switch (expression) {
            case ("sum"): {
                return lowerBound * problemData.getConfiguration().getNumberOfRows();
            }
            case ("avg"): {
                return lowerBound;
            }
            case ("min"): {
                return lowerBound;
            }
            case ("max"): {
                return lowerBound;
            }
            case ("abs"): {
                if (lowerBound < 0 && upperBound > 0) {
                    return 0.0;
                }
                return Math.min(Math.abs(lowerBound), Math.abs(upperBound));
            }
        }
        return Double.NaN;
    }

    @Override
    public double getMaxValue(ProblemData problemData) {
        double lowerBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getTotalLowerBound();
        double upperBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getTotalUpperBound();
        switch (expression) {
            case ("sum"): {
                return upperBound * problemData.getConfiguration().getNumberOfRows();
            }
            case ("avg"): {
                return upperBound;
            }
            case ("min"): {
                return upperBound;
            }
            case ("max"): {
                return upperBound;
            }
            case ("abs"): {
                return Math.max(Math.abs(upperBound), Math.abs(lowerBound));
            }
        }
        return Double.NaN;
    }

    @Override
    public double getNextMinValue(ProblemData problemData) {
        double lowerBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getTotalLowerBound();
        double upperBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getTotalUpperBound();
        double[] doubleValues = new double[this.valueList.size() + 1];
        double sum = 0.0;
        for(int i = 0 ; i < doubleValues.length - 1 ; i++) {
            doubleValues[i] = this.valueList.get(i);
            sum += doubleValues[i];
        }
        sum += lowerBound;
        doubleValues[this.valueList.size()] = lowerBound;
        switch (expression) {
            case ("sum"): {
                return sum;
            }
            case ("avg"): {
                return sum / doubleValues.length;
            }
            case ("min"): {
                return ValueCompression.getMin(doubleValues);
            }
            case ("max"): {
                return ValueCompression.getMax(doubleValues);
            }
            case ("abs"): {
                if (lowerBound < 0 && upperBound > 0) {
                    return 0.0;
                }
                return Math.min(Math.abs(lowerBound), Math.abs(upperBound));
            }
        }
        return Double.NaN;
    }

    @Override
    public double getNextMaxValue(ProblemData problemData) {
        double lowerBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getTotalLowerBound();
        double upperBound = ((NumericDataType) problemData.getTypeForName(this.dataTypeName)).getTotalUpperBound();
        double[] doubleValues = new double[this.valueList.size() + 1];
        double sum = 0.0;
        for(int i = 0 ; i < doubleValues.length - 1 ; i++) {
            doubleValues[i] = this.valueList.get(i);
            sum += doubleValues[i];
        }
        sum += upperBound;
        doubleValues[this.valueList.size()] = upperBound;
        switch (expression) {
            case ("sum"): {
                return sum;
            }
            case ("avg"): {
                return sum / doubleValues.length;
            }
            case ("min"): {
                return ValueCompression.getMin(doubleValues);
            }
            case ("max"): {
                return ValueCompression.getMax(doubleValues);
            }
            case ("abs"): {
                if (lowerBound < 0 && upperBound > 0) {
                    return 0.0;
                }
                return Math.max(Math.abs(upperBound), Math.abs(lowerBound));
            }
        }
        return Double.NaN;
    }
}
