package org.bissis.dm.generate.expressions;

import org.bissis.dm.generate.data.ProblemData;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * This is a simple expression for a single numeric value.
 * This value will be returned every time the expression is evaluated.
 * Both data types are empty since no attributes are used.
 * @author Markus Ullrich
 */
public class SimpleNumberExpression implements IExpression {

    private double number;

    private DecimalFormat format;

    public SimpleNumberExpression(double number, boolean integerMode) {
        this.number = number;
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
        return format.format(number);
    }

    @Override
    public String getExpression() {
        return "";
    }

    @Override
    public String getDataTypeName() {
        return null;
    }

    @Override
    public String getSecondDataTypeName() {
        return null;
    }

    @Override
    public double getMinValue(ProblemData problemData) {
        return number;
    }

    @Override
    public double getMaxValue(ProblemData problemData) {
        return number;
    }

    @Override
    public double getNextMinValue(ProblemData problemData) {
        return number;
    }

    @Override
    public double getNextMaxValue(ProblemData problemData) {
        return number;
    }
}
