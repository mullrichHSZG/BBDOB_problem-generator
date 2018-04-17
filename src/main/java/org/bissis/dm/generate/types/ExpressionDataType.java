package org.bissis.dm.generate.types;

import org.bissis.dm.generate.expressions.ArithmeticExpression;
import org.bissis.dm.generate.expressions.IExpression;
import org.bissis.dm.generate.data.ProblemData;
import org.bissis.dm.generate.expressions.MethodExpression;

/**
 * Created by bissi on 13.03.2018.
 */
public class ExpressionDataType extends NumericDatatype {

    private IExpression expression;
    private ProblemData problemData;
    private boolean statisticalExpression;

    private String typeName;
    private Expression expressionType;
    private String secondPart;

    private String lastValue;

    private boolean useLastValue;

    private boolean integerMode;

    /**
     * This data type represents an expression. Therefore, it will not generate random values but values based on other types.
     * Whe using this type, please make sure that you ensure, that the other types used in the expression, are present in the problemData
     * and that these types are an instance of NumericDataType.
     * @param expression - the expression as String value. The format of the expression needs to be either a 3-digit function
     *                   like min or max followed by brackets enclosing the referenced data type or an attribute/value followed by
     *                   a space, an arithmetic operator (one of {+,-,*,/}), another space and a final attribute/value.
     * @param name - the name of this data type
     * @param problemData - the problem data which contains all attributes and their generated values
     */
    public ExpressionDataType(String expression, String name, ProblemData problemData) {
        super(name);
        this.problemData = problemData;
        String[] expressionParts = expression.split(" ");
        if(expressionParts.length == 1) {
            //min, max, sum, avg, abs
            this.statisticalExpression = true;
            String expressionString = expressionParts[0];
            if (expressionString.startsWith("sum")) {
                this.expressionType = Expression.SUM;
            } else if (expressionString.startsWith("avg")) {
                this.expressionType = Expression.AVG;
            } else if (expressionString.startsWith("min")) {
                this.expressionType = Expression.MIN;
            } else if (expressionString.startsWith("max")) {
                this.expressionType = Expression.MAX;
            }
            this.typeName = expressionString.substring(4,expressionString.indexOf(")"));
            this.expression = new MethodExpression(this.typeName, expressionString.substring(0,3));
        } else if (expressionParts.length == 3){
            // plus, minus, mult, div
            this.statisticalExpression = false;
            String firstPart = expressionParts[0];
            String operator = expressionParts[1];
            String secondPart = expressionParts[2];
            boolean orderSwitched = false;
            boolean integerMode = false;
            if (operator.equals("+")) {
                this.expressionType = Expression.PLUS;
            } else if (operator.equals("-")) {
                this.expressionType = Expression.MINUS;
            } else if (operator.equals("*")) {
                this.expressionType = Expression.MULT;
            } else if (operator.equals("/")) {
                this.expressionType = Expression.DIV;
            }
            for (String dataType : this.problemData.getDataTypes()) {
                if (dataType.equals(firstPart)) {
                    this.typeName = firstPart;
                    for (String dataType2 : this.problemData.getDataTypes()) {
                        if (dataType2.equals(secondPart)) {
                            this.secondPart = secondPart;
                            integerMode = ((NumericDatatype)this.problemData.getTypeForName(this.typeName)).allowIntegersOnly();
                            this.expression = new ArithmeticExpression(this.typeName, secondPart, operator, integerMode, orderSwitched);
                            break;
                        }
                    }
                    break;
                }
            }
            if (this.expression == null) {
                if (!firstPart.equals(this.typeName)) {
                    orderSwitched = true;
                    for (String dataType : this.problemData.getDataTypes()) {
                        if (dataType.equals(secondPart)) {
                            this.typeName = secondPart;
                            this.secondPart = firstPart;
                            integerMode = ((NumericDatatype) this.problemData.getTypeForName(this.typeName)).allowIntegersOnly();
                            this.expression = new ArithmeticExpression(this.typeName, operator, Double.parseDouble(firstPart), integerMode, orderSwitched);
                            break;
                        }
                    }
                } else {
                    this.secondPart = null;
                    this.expression = new ArithmeticExpression(this.typeName, operator, Double.parseDouble(secondPart), integerMode, orderSwitched);
                }
            }
        }
    }

    @Override
    public String generateValue() {
        if (useLastValue) {
            useLastValue = false;
            return lastValue;
        }
        String first = problemData.getLastValueFor(this.typeName);
        if (first != null && first.equals("false"))
            first = "0.0";
        if (first != null && first.equals("true"))
            first = "1.0";
        String second = this.secondPart == null? null : problemData.getLastValueFor(this.secondPart);
        if (second != null && second.equals("false"))
            second = "0.0";
        if (second != null && second.equals("true"))
            second = "1.0";
        this.lastValue = expression.evaluate(first, second);
        return this.lastValue;
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public void resetLastValue() {
        this.useLastValue = true;
    }

    @Override
    public long getTotalDistinctValues() {
        if (this.statisticalExpression)
            return 1;
        long firstValue = 1;
        long secondValue = 1;
        if (this.typeName != null) {
            IGenerateDataType firstType = this.problemData.getTypeForName(this.typeName);
            if (firstType != null) {
                firstValue = firstType.getTotalDistinctValues();
            }
        }
        if (this.secondPart != null) {
            IGenerateDataType secondType = this.problemData.getTypeForName(this.secondPart);
            if (secondPart != null) {
                secondValue = secondType.getTotalDistinctValues();
            }
        }
        if ((firstValue) * (secondValue) >= Integer.MAX_VALUE) {
            return Long.MAX_VALUE;
        } else {
            return firstValue * secondValue;
        }
    }

    @Override
    public long getNextDistinctValues() {
        if (this.statisticalExpression)
            return 1;
        long firstValue = 1;
        long secondValue = 1;
        if (this.typeName != null) {
            IGenerateDataType firstType = this.problemData.getTypeForName(this.typeName);
            if (firstType != null) {
                firstValue = firstType.getNextDistinctValues();
            }
        }
        if (this.secondPart != null) {
            IGenerateDataType secondType = this.problemData.getTypeForName(this.secondPart);
            if (secondPart != null) {
                secondValue = secondType.getNextDistinctValues();
            }
        }
        if ((firstValue) * (secondValue) >= Integer.MAX_VALUE) {
            return Long.MAX_VALUE;
        } else {
            return firstValue * secondValue;
        }
    }

    @Override
    public double getTotalUpperBound() {
        return this.expression.getMaxValue(this.problemData);
    }

    @Override
    public double getTotalLowerBound() {
        return this.expression.getMinValue(this.problemData);
    }

    @Override
    public double getNextUpperBound() {
        return getTotalUpperBound(); //TODO: it might not be the same value, depending on the expression, so another method needs to be provided for those as well
    }

    @Override
    public double getNextLowerBound() {
        return getTotalLowerBound(); //TODO: same problem as with the next total upper bound
    }

    @Override
    public boolean allowIntegersOnly() {
        return ((NumericDatatype)this.problemData.getTypeForName(this.typeName)).allowIntegersOnly();
    }

    public enum Expression {
        MULT,
        DIV,
        MINUS,
        PLUS,
        MIN,
        MAX,
        SUM,
        AVG,
        ABS
    }

    public IExpression getExpression(){
        return this.expression;
    }

}
