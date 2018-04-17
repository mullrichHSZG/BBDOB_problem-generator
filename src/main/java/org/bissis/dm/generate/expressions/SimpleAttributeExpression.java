package org.bissis.dm.generate.expressions;

import org.bissis.dm.generate.data.ProblemData;
import org.bissis.dm.generate.types.NumericDatatype;

/**
 * This expression represents a single attribute
 * therefore, it will evaluate to the value of
 * the attribute only.
 * Created by bissi on 14.03.2018.
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
        return ((NumericDatatype) problemData.getTypeForName(this.name)).getTotalLowerBound();
    }

    @Override
    public double getMaxValue(ProblemData problemData) {
        return ((NumericDatatype) problemData.getTypeForName(this.name)).getTotalUpperBound();
    }
}
