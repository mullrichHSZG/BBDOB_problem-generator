package org.bissis.dm.generate.expressions;

import org.bissis.dm.generate.data.ProblemData;
import org.bissis.dm.generate.types.NumericDatatype;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Represents an arithmetic expression
 * for example: x * y
 * whereas x and y can represent any value
 * or an identifier for a data type.
 * However, at least one value has to
 * represent an identifier.
 * Created by bissi on 13.03.2018.
 */
public class ArithmeticExpression implements IExpression {

    private double secondValue;
    private boolean useSecondValue, orderSwitched;

    private String dataTypeName, secondDataTypeName, expression;

    private DecimalFormat format;

    public ArithmeticExpression(String dataTypeName, String secondDataTypeName, String expression, boolean integerMode, boolean orderSwitched) {
        this.useSecondValue = true;
        this.dataTypeName = dataTypeName;
        this.secondDataTypeName = secondDataTypeName;
        this.expression = expression;
        this.orderSwitched = orderSwitched;
        if(integerMode){
            this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
            this.format.applyPattern("0");
        }else{
            this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
            this.format.applyPattern("0.00");
        }
    }

    public ArithmeticExpression(String dataTypeName, String expression, double secondValue, boolean integerMode, boolean orderSwitched) {
        this.useSecondValue = false;
        this.dataTypeName = dataTypeName;
        this.secondValue = secondValue;
        this.expression = expression;
        this.orderSwitched = orderSwitched;
        if(integerMode){
            this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
            this.format.applyPattern("0");
        }else{
            this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
            this.format.applyPattern("0.00");
        }
    }

    @Override
    public String evaluate(String value, String secondValue) {
        if (useSecondValue) {
            switch (this.expression) {
                case ("+"): {
                    return this.format.format((Double.parseDouble(value) + Double.parseDouble(secondValue)));
                }
                case ("-"): {
                    if (orderSwitched)
                        return "" + (Double.parseDouble(secondValue) - Double.parseDouble(value));
                    return this.format.format((Double.parseDouble(value) - Double.parseDouble(secondValue)));
                }
                case ("*"): {
                    return this.format.format((Double.parseDouble(value) * Double.parseDouble(secondValue)));
                }
                case ("/"): {
                    if (orderSwitched)
                        return "" + (Double.parseDouble(secondValue) / Double.parseDouble(value));
                    return this.format.format((Double.parseDouble(value) / Double.parseDouble(secondValue)));
                }
            }
        } else {
            switch (this.expression) {
                case ("+"): {
                    return this.format.format((Double.parseDouble(value) + this.secondValue));
                }
                case ("-"): {
                    if (orderSwitched)
                        return "" + (this.secondValue - Double.parseDouble(value));
                    return this.format.format((Double.parseDouble(value) - this.secondValue));
                }
                case ("*"): {
                    return this.format.format((Double.parseDouble(value) * this.secondValue));
                }
                case ("/"): {
                    if (orderSwitched)
                        return "" + this.secondValue / Double.parseDouble(value);
                    return this.format.format((Double.parseDouble(value) / this.secondValue));
                }
            }
        }
        return "" + (Double.NaN);
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
        return secondDataTypeName;
    }

    @Override
    public double getMinValue(ProblemData problemData) {
        double returnValue = Double.NaN;
        double lowerBound = ((NumericDatatype) problemData.getTypeForName(this.dataTypeName)).getTotalLowerBound();
        double upperBound = ((NumericDatatype) problemData.getTypeForName(this.dataTypeName)).getTotalUpperBound();
        if (useSecondValue) {
            double secondLowerBound = ((NumericDatatype) problemData.getTypeForName(this.secondDataTypeName)).getTotalLowerBound();
            double secondUpperBound = ((NumericDatatype) problemData.getTypeForName(this.secondDataTypeName)).getTotalUpperBound();
            returnValue = evaluateMinExpression(returnValue, lowerBound, upperBound, secondLowerBound, secondUpperBound);
        } else {
            returnValue = evaluateMinExpression(returnValue, lowerBound, upperBound, secondValue, secondValue);
        }
        return Double.parseDouble(this.format.format(returnValue));
    }

    private double evaluateMinExpression(double returnValue, double lowerBound, double upperBound, double secondLowerBound, double secondUpperBound) {
        switch (this.expression) {
            case ("+"): {
                returnValue = lowerBound + secondLowerBound;
                break;
            }
            case ("-"): {
                if (orderSwitched)
                    returnValue = secondLowerBound - upperBound;
                else
                    returnValue = lowerBound - secondUpperBound;
                break;
            }
            case ("*"): {
                returnValue = lowerBound * secondLowerBound;
                break;
            }
            case ("/"): {
                if (orderSwitched)
                    returnValue = secondLowerBound / upperBound;
                else
                    returnValue = lowerBound / secondUpperBound;
                break;
            }
        }
        return returnValue;
    }

    @Override
    public double getMaxValue(ProblemData problemData) {
        double returnValue = Double.NaN;
        double lowerBound = ((NumericDatatype) problemData.getTypeForName(this.dataTypeName)).getTotalLowerBound();
        double upperBound = ((NumericDatatype) problemData.getTypeForName(this.dataTypeName)).getTotalUpperBound();
        if (useSecondValue) {
            double secondLowerBound = ((NumericDatatype) problemData.getTypeForName(this.secondDataTypeName)).getTotalLowerBound();
            double secondUpperBound = ((NumericDatatype) problemData.getTypeForName(this.secondDataTypeName)).getTotalUpperBound();
            returnValue = evaluateMinExpression(returnValue, upperBound, lowerBound, secondUpperBound, secondLowerBound);
        } else {
            returnValue = evaluateMinExpression(returnValue, lowerBound, upperBound, secondValue, secondValue);
        }
        return Double.parseDouble(this.format.format(returnValue));
    }
}
