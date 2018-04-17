package org.bissis.dm.generate.data;

import org.bissis.dm.generate.expressions.*;
import org.bissis.dm.generate.types.ExpressionDataType;
import org.bissis.dm.generate.types.NumericDataType;
import org.json.simple.JSONObject;

/**
 * Class for storing a constraint used by the problem instance generator.
 * @author Markus Ullrich
 */
@SuppressWarnings("unused")
public class ProblemGenerationConstraint {

    private String name;
    private Scope scope;
    private IExpression left, right; //can be a function or just a variable or a number
    private Relation relation;
    private ProblemData problemData;

    public ProblemGenerationConstraint(ExpressionDataType expressionDataType, ProblemData problemData) {
        this.problemData = problemData;
        this.name = expressionDataType.getName() + "_equals_itself";
        this.scope = Scope.ROW;
        this.left = new SimpleAttributeExpression(expressionDataType.getName());
        this.relation = Relation.E;
        this.right = expressionDataType.getExpression();
    }

    public ProblemGenerationConstraint(JSONObject jsonConstraint, ProblemData problemData) {
        this.problemData = problemData;
        this.name = (String) jsonConstraint.get("name");
        /*String scope = (String) jsonConstraint.get("scope"); //TODO: for now the scope will be ignored
        switch (scope) {
            case ("row"): {
                this.scope = Scope.ROW;
                break;
            }
            case("before"): {
                this.scope = Scope.BEFORE;
                break;
            }
            case("after"): {
                this.scope = Scope.AFTER;
                break;
            }
            case("global"): {
                this.scope = Scope.GLOBAL;
                break;
            }
            default: {
                this.scope= Scope.GLOBAL;
                break;
            }
        }*/
        String relation = (String) jsonConstraint.get("relation");
        switch (relation) {
            case (">"): {
                this.relation = Relation.GT;
                break;
            }
            case("<"): {
                this.relation = Relation.LT;
                break;
            }
            case(">="): {
                this.relation = Relation.GE;
                break;
            }
            case("<="): {
                this.relation = Relation.LE;
                break;
            }
            case("="): {
                this.relation = Relation.E;
                break;
            }
            case("!="): {
                this.relation = Relation.NE;
                break;
            }
            default: {
                this.relation = Relation.E;
                break;
            }
        }
        JSONObject jsonLeft = (JSONObject) jsonConstraint.get("left");
        String leftType = (String) jsonLeft.get("type");
        switch (leftType) {
            case ("expression"): {
                String value = (String) jsonLeft.get("value");
                //TODO: duplicated code, see comment below
                String[] expressionParts = value.split(" ");
                if (expressionParts.length == 1) {
                    //min, max, sum, avg, abs
                    String expressionString = expressionParts[0];
                    String typeName = expressionString.substring(4, expressionString.indexOf(")"));
                    this.left = new MethodExpression(typeName, expressionString.substring(0, 3));
                } else if (expressionParts.length == 3) {
                    // plus, minus, mult, div
                    String typeName = null;
                    String firstPart = expressionParts[0];
                    String operator = expressionParts[1];
                    String secondPart = expressionParts[2];
                    for (String dataType : this.problemData.getDataTypes()) {
                        if (dataType.equals(firstPart)) {
                            typeName = firstPart;
                            for (String dataType2 : this.problemData.getDataTypes()) {
                                if (dataType2.equals(secondPart)) {
                                    boolean integerMode = ((NumericDataType)this.problemData.getTypeForName(typeName)).allowIntegersOnly();
                                    this.left = new ArithmeticExpression(typeName, secondPart, operator, integerMode, false);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (this.left == null) {
                        if (!firstPart.equals(typeName)) {
                            //the order of the operands is different than the arithmetic expression expects
                            for (String dataType : this.problemData.getDataTypes()) {
                                if (dataType.equals(secondPart)) {
                                    typeName = secondPart;
                                    boolean integerMode = ((NumericDataType) this.problemData.getTypeForName(typeName)).allowIntegersOnly();
                                    this.left = new ArithmeticExpression(typeName, operator, Double.parseDouble(firstPart), integerMode, true);
                                    break;
                                }
                            }
                        } else {
                            //no integer mode is the default
                            this.left = new ArithmeticExpression(typeName, operator, Double.parseDouble(secondPart), false, false);
                        }
                    }
                }
                break;
            }
            case ("integer"): {
                long value = (Long) jsonLeft.get("value");
                this.left = new SimpleNumberExpression(value, true);
                break;
            }
            case ("double"): {
                double value = (Double) jsonLeft.get("value");
                this.left = new SimpleNumberExpression(value, false);
                break;
            }
            case ("attribute"): {
                String value = (String) jsonLeft.get("value");
                this.left = new SimpleAttributeExpression(value);
                break;
            }
        }
            JSONObject jsonRight = (JSONObject) jsonConstraint.get("right");
            String rightType = (String) jsonRight.get("type");
            switch (rightType) {
                case ("expression"): {
                    String value = (String) jsonRight.get("value");
                    String[] expressionParts = value.split(" ");
                    if(expressionParts.length == 1) {
                        //min, max, sum, avg
                        String expressionString = expressionParts[0];
                        String typeName = expressionString.substring(4,expressionString.indexOf(")"));
                        this.right = new MethodExpression(typeName, expressionString.substring(0,3));
                    } else if (expressionParts.length == 3){
                        // plus, minus, mult, div
                        String typeName = null;
                        String firstPart = expressionParts[0];
                        String operator = expressionParts[1];
                        String secondPart = expressionParts[2];
                        for (String dataType : this.problemData.getDataTypes()) {
                            if (dataType.equals(firstPart)) {
                                typeName = firstPart;
                                for (String dataType2 : this.problemData.getDataTypes()) {
                                    if (dataType2.equals(secondPart)) {
                                        boolean integerMode = ((NumericDataType)this.problemData.getTypeForName(typeName)).allowIntegersOnly();
                                        this.right = new ArithmeticExpression(typeName, secondPart, operator, integerMode, false);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        if (this.right == null) {
                            if (!firstPart.equals(typeName)) {
                                //the same as above applies here as well
                                for (String dataType : this.problemData.getDataTypes()) {
                                    if (dataType.equals(secondPart)) {
                                        typeName = secondPart;
                                        boolean integerMode = ((NumericDataType) this.problemData.getTypeForName(typeName)).allowIntegersOnly();
                                        this.right = new ArithmeticExpression(typeName, operator, Double.parseDouble(firstPart), integerMode, true);
                                        break;
                                    }
                                }
                            } else {
                                this.right = new ArithmeticExpression(typeName, operator, Double.parseDouble(secondPart), false, false);
                            }
                        }
                    }
                    break;
                }
                case ("integer"): {
                    long value = (Long) jsonRight.get("value");
                    this.right = new SimpleNumberExpression(value, true);
                    break;
                }
                case ("double"): {
                    double value = (Double) jsonRight.get("value");
                    this.right = new SimpleNumberExpression(value, false);
                    break;
                }
                case ("attribute"): {
                    String value = (String) jsonRight.get("value");
                    this.right = new SimpleAttributeExpression(value);
                    break;
                }
        }
    }

    public Scope getScope(){
        return scope;
    }

    public String getName(){
        return name;
    }

    public Relation getRelation(){
        return relation;
    }

    public IExpression getLeft() {
        return left;
    }

    public IExpression getRight() {
        return right;
    }

    public boolean isPossible(){
        switch (this.relation) {
            case GT: {
                return this.left.getMaxValue(problemData) > this.right.getMinValue(problemData);
            }
            case LT: {
                return this.left.getMinValue(problemData) < this.right.getMaxValue(problemData);
            }
            case GE: {
                return this.left.getMaxValue(problemData) >= this.right.getMinValue(problemData);
            }
            case LE: {
                return this.left.getMinValue(problemData) <= this.right.getMaxValue(problemData);
            }
            case E: {
                if (this.left.getMaxValue(problemData) >= this.right.getMinValue(problemData)) {
                    return this.left.getMinValue(problemData) <= this.right.getMaxValue(problemData);
                }
                if (this.right.getMaxValue(problemData) >= this.left.getMinValue(problemData)) {
                    return this.right.getMinValue(problemData) <= this.left.getMaxValue(problemData);
                }
                break;
            }
            case NE: {
                //noinspection RedundantIfStatement
                if (this.left.getMinValue(problemData) == this.right.getMinValue(problemData) &&
                        this.left.getMaxValue(problemData) == this.right.getMaxValue(problemData) &&
                        this.left.getMinValue(problemData) == this.right.getMaxValue(problemData)) {
                    return false;
                }else {
                    return true;
                }
            }
            default: {
                return false;
            }
        }
        return false;
    }

    boolean isStillPossible(long remainingRows, String currentLeft, String currentRight, ProblemData problemData) {
        //TODO: works only if all values are numeric for now
        if (currentRight == null) {
            return true;
        }
        double leftValue = Double.parseDouble(currentLeft);//this.left.evaluate(currentLeft, problemData.getLastValueFor(this.left.getSecondDataTypeName())));

        //Only in case the constraint is global!
        /*
        NumericDataType leftType = (NumericDataType) problemData.getTypeForName(this.left.getDataTypeName());
        switch (this.left.getExpression()) {
            case ("sum"): {
                leftValue += remainingRows *  leftType.getTotalUpperBound();
                break;
            }
            case ("min"): {
                if (this.relation == Relation.LT || this.relation == Relation.LE) {
                    leftValue = leftType.getTotalLowerBound();
                }
                break;
            }
            case ("max"): {
                if (this.relation == Relation.GT || this.relation == Relation.GE) {
                    leftValue = leftType.getTotalUpperBound();
                }
                break;
            }
            case ("avg"): {
                if (this.relation == Relation.GT || this.relation == Relation.GE) {
                    leftValue = (leftValue + remainingRows * leftType.getTotalUpperBound()) / (remainingRows + 1);
                }
                if (this.relation == Relation.LT || this.relation == Relation.LE) {
                    leftValue = (leftValue + remainingRows * leftType.getTotalLowerBound()) / (remainingRows + 1);
                }
                break;
            }
        }*/

        double rightValue = Double.parseDouble(currentRight);//this.right.evaluate(currentRight, problemData.getLastValueFor(this.right.getSecondDataTypeName())));

        //Only in case the constraint is global!
        /*
        NumericDataType rightType = (NumericDataType) problemData.getTypeForName(this.right.getDataTypeName());
        switch (this.right.getExpression()) {
            case ("sum"): {
                rightValue += remainingRows *  rightType.getTotalUpperBound();
                break;
            }
            case ("min"): {
                if (this.relation == Relation.GT || this.relation == Relation.GE) {
                    rightValue = rightType.getTotalLowerBound();
                }
                break;
            }
            case ("max"): {
                if (this.relation == Relation.LT || this.relation == Relation.LE) {
                    rightValue = rightType.getTotalUpperBound();
                }
                break;
            }
            case ("avg"): {
                if (this.relation == Relation.LT || this.relation == Relation.LE) {
                    rightValue = (rightValue + remainingRows * rightType.getTotalUpperBound()) / (remainingRows + 1);
                }
                if (this.relation == Relation.GT || this.relation == Relation.GE) {
                    rightValue = (rightValue + remainingRows * rightType.getTotalLowerBound()) / (remainingRows + 1);
                }
                break;
            }
        } */

        if (this.relation == Relation.E) {
            //TODO: please refrain from using avg / sum and equals in combination
            return leftValue == rightValue;
        }
        if (this.relation == Relation.NE) {
            return leftValue != rightValue;
        }
        if (this.relation == Relation.GT) {
            return leftValue > rightValue;
        }
        if (this.relation == Relation.GE) {
            return leftValue >= rightValue;
        }
        if (this.relation == Relation.LT) {
            return leftValue < rightValue;
        }
        //noinspection SimplifiableIfStatement
        if (this.relation == Relation.LE) {
            return leftValue <= rightValue;
        }
        return true;
    }

    private enum Scope {
        ROW,
        BEFORE,
        AFTER,
        GLOBAL
    }

    private enum Relation {
        GT,
        LT,
        GE,
        LE,
        E,
        NE
    }

}