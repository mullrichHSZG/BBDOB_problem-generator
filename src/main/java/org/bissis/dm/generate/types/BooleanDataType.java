package org.bissis.dm.generate.types;

import java.util.Random;

/**
 * Class for a data type that generates boolean values only.
 * Further, the respective numeric representations (0,1) can be generated as well.
 * @author Markus Ullrich
 */
public class BooleanDataType extends NumericDataType {

    private Random random;
    private double truePercentage = 0.5;

    private boolean defaultValue, changeTruePercentage, useLastValue;

    private String lastValue;

    /**
     * Creates a new boolean data type.
     * @param name - the name of this type
     * @param truePercentage - changes the default probability of the value 'true' being generated from 0.5 to the provided percentage, needs to be between 0.0 and 1.0
     * @param random - the random number generator that should be used by this data type
     * @throws IllegalArgumentException - if the value of truePercentage is not between 0.0 and 1.0
     */
    public BooleanDataType(String name, double truePercentage, Random random) {
        super(name);
        if (truePercentage < 0.0 || truePercentage > 1.0) {
            throw new IllegalArgumentException("The new probability for 'true' to be generated must be between 0.0 and 1.0");
        }
        this.random = random;
        this.truePercentage = truePercentage;
        if (truePercentage > 0.5) {
            this.defaultValue = true;
        }
        if (truePercentage < 0.5) {
            this.defaultValue = false;
        }
        this.changeTruePercentage = true;
        this.lastValue = "true";
    }

    /**
     * Creates a new boolean data type.
     * @param name - the name of this type
     * @param random - the random number generator that should be used by this data type
     * @throws IllegalArgumentException - if the value of truePercentage is not between 0.0 and 1.0
     */
    public BooleanDataType(String name, Random random) {
        super(name);
        this.random = random;
    }

    /**
     * Changes the default value for this generator.
     * @param defaultValue - the new default value
     */
    @SuppressWarnings("WeakerAccess")
    public void setDefaultValue(boolean defaultValue) {
        if (changeTruePercentage) {
            if (truePercentage > 0.5 && !defaultValue) {
                throw new IllegalArgumentException("Cannot set the default value to false with a true percentage higher than 0.5");
            }
            if (truePercentage < 0.5 && defaultValue) {
                throw new IllegalArgumentException("Cannot set the default value to true with a true percentage lower than 0.5");
            }
        }else {
            this.defaultValue = defaultValue;
        }
    }

    @Override
    public String generateValue() {
        if (!useLastValue) {
            //generate new values
            if (!changeTruePercentage) this.lastValue =  "" + this.random.nextBoolean();
            else this.lastValue = "" + (random.nextDouble() <= truePercentage);
        } else {
            this.useLastValue = false;
        }
        return this.lastValue;
    }

    @Override
    public String getDefaultValue() {
        return "" + defaultValue;
    }

    @Override
    public void resetLastValue() {
        this.useLastValue = true;
    }

    @Override
    public long getTotalDistinctValues() {
        if (this.truePercentage >= 1.0 || this.truePercentage <= 0.0) {
            return 1;
        }
        return 2;
    }

    @Override
    public long getNextDistinctValues() {
        return getTotalDistinctValues();
    }

    @Override
    public double generateNumericValue(){
        if (Boolean.parseBoolean(generateValue())) {
            return 1.0;
        }
        return 0.0;
    }

    @Override
    public double defaultNumericValue(){
        if (Boolean.parseBoolean(getDefaultValue())) {
            return 1.0;
        }
        return 0.0;
    }

    @Override
    public double getTotalUpperBound() {
        if (changeTruePercentage && truePercentage <= 0.0) {
            return 0.0;
        }
        return 1.0;
    }

    @Override
    public double getTotalLowerBound() {
        if (changeTruePercentage && truePercentage >= 1.0) {
            return 1.0;
        }
        return 0.0;
    }

    @Override
    public double getNextUpperBound() {
        return getTotalUpperBound();
    }

    @Override
    public double getNextLowerBound() {
        return getTotalLowerBound();
    }

    @Override
    public boolean allowIntegersOnly() {
        return true;
    }

}
