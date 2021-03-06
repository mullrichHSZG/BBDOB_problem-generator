package org.ead.generate.expressions;

import org.ead.generate.types.NumericDataType;
import org.ead.generate.data.ProblemData;

/**
 * This expression represents a single attribute
 * therefore, it will evaluate to the value of
 * the attribute only.
 * @author Markus Ullrich
 */
public class SimpleAttributeExpression implements IExpression {

    private String name;

    public SimpleAttributeExpression(String name) {
        this.name = name;
    }

    @Override
    public String evaluate(String value, String secondValue) {
        return value;
    }

    @Override
    public String getExpression() {
        return "";
    }

    @Override
    public String getDataTypeName() {
        return name;
    }

    @Override
    public String getSecondDataTypeName() {
        return null;
    }

    @Override
    public double getMinValue(ProblemData problemData) {
        return ((NumericDataType) problemData.getTypeForName(this.name)).getTotalLowerBound();
    }

    @Override
    public double getMaxValue(ProblemData problemData) {
        return ((NumericDataType) problemData.getTypeForName(this.name)).getTotalUpperBound();
    }

    @Override
    public double getNextMinValue(ProblemData problemData) {
        return ((NumericDataType) problemData.getTypeForName(this.name)).getNextLowerBound();
    }

    @Override
    public double getNextMaxValue(ProblemData problemData) {
        return ((NumericDataType) problemData.getTypeForName(this.name)).getNextUpperBound();
    }
}
