package org.bissis.dm.generate.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class functions similar to an {@link IDGenerateDataType}
 * however, the increment for the next generated value is random
 * and in between the provided boundaries.
 * Created by bissi on 24.11.2017.
 */
public class RandomStepDataType extends NumericDatatype {

    private long start, minIncrement, maxIncrement, lastIncrement, stop;
    private boolean preGenerated;
    private List<Long> preGeneratedValues;
    private int index;

    private Random random;

    /**
     * Creates a new {@link RandomStepDataType}
     * @param typeName - the name of this data type
     * @param start - the first value that will be generated by this data type
     * @param minIncrement - the minimum value for the next increment - needs to be larger than zero
     * @param maxIncrement - the maximum value for the next increment - needs to be at least as high as the minIncrement
     */
    public RandomStepDataType(String typeName, long start, long minIncrement, long maxIncrement, Random random) {
        super(typeName);
        this.start = start;
        if (minIncrement <= 0) {
            throw new IllegalArgumentException("The minimum increment must be larger than zero!");
        }
        this.minIncrement = minIncrement;
        if (maxIncrement <= minIncrement) {
            throw new IllegalArgumentException("The maximum increment must be equal to or larger than the min increment!");
        }
        this.maxIncrement = maxIncrement;
        this.lastIncrement = 0;
        this.random = random;
        this.preGenerated = false;
    }

    /**
     * Creates a new {@link RandomStepDataType} and attempts to pre-generate the values that have to be generated based on stop,
     * which represents the last value, and rows, which represents the total amount of values to be generated.
     * Careful, every new value that will be generated after the method has been called as often as the number of rows, the last value will be repeatedly generated.
     * @param typeName - the name of this data type
     * @param start - the first value that will be generated by this data type
     * @param minIncrement - the minimum value for the next increment - needs to be larger than zero
     */
    public RandomStepDataType(String typeName, long start, long minIncrement, Random random, long stop, long rows) {
        super(typeName);
        this.start = start;
        this.stop = stop;
        if (minIncrement <= 0) {
            throw new IllegalArgumentException("The minimum increment must be larger than zero!");
        }
        rows--; //adjust the number of rows, since it should be the number of additional rows with the first row containing the start value
        if (rows < 0) {
            throw new IllegalArgumentException("The number of rows must be larger than zero!");
        }
        this.minIncrement = minIncrement;
        this.maxIncrement = Math.max(minIncrement, Math.abs(stop - start) / (rows == 0 ? 1 : rows));
        this.lastIncrement = 0;
        this.random = random;
        this.preGenerated = true;
        this.preGeneratedValues = new ArrayList<>();
        this.preGeneratedValues.add(this.start);
        for (int i = 1 ; i < rows ; i++) {
            long increment = minIncrement;
            if (maxIncrement - minIncrement > 0) {
                increment = random.nextInt((int) (maxIncrement-minIncrement)) + minIncrement;
            }
            preGeneratedValues.add(increment);
            this.start += increment;
            this.lastIncrement = increment;
            maxIncrement = Math.max(minIncrement, Math.abs(stop - this.start) / (rows - i));
        }
        this.preGeneratedValues.add(stop - this.start);
        this.preGeneratedValues.add((long) 0); // to ensure that the last value gets generated over and over again
        this.start = this.preGeneratedValues.get(0);
        this.index = 1;
    }

    public String generateValue() {
        String returnValue = "" + start;
        long increment;
        if (this.preGenerated) {
            increment = this.preGeneratedValues.get(this.index);
            if (this.index < this.preGeneratedValues.size() - 1)
                this.index++;
        } else {
            increment = random.nextInt((int) (maxIncrement - minIncrement)) + minIncrement;
        }
        this.lastIncrement = increment;
        start += increment;
        return returnValue;
    }

    public String getDefaultValue() {
        return "" + this.start;
    }

    @Override
    public void resetLastValue() {
        this.start -= lastIncrement;
        this.lastIncrement = 0;
        this.index--; //careful, this method is not idempotent
    }

    @Override
    public long getTotalDistinctValues() {
        if (preGenerated) {
            return this.preGeneratedValues.size() - 1;
        }
        return Long.MAX_VALUE / this.minIncrement - this.start / this.minIncrement;
    }

    @Override
    public long getNextDistinctValues() {
        if (this.preGenerated) {
            return 1;
        } else {
            return this.maxIncrement - this.minIncrement;
        }
    }

    @Override
    public double getTotalUpperBound() {
        if (preGenerated) {
            return this.stop;
        }
        return Long.MAX_VALUE;
    }

    @Override
    public double getTotalLowerBound() {
        return this.start;
    }

    @Override
    public double getNextUpperBound() {
        if (this.preGenerated) {
            if (this.index >= this.preGeneratedValues.size()) {
                return this.start + this.preGeneratedValues.get(this.preGeneratedValues.size() - 1);
            } else {
                return this.start + this.preGeneratedValues.get(this.index + 1);
            }
        }
        return this.start + this.maxIncrement;
    }

    @Override
    public double getNextLowerBound() {
        if (this.preGenerated) {
            if (this.index >= this.preGeneratedValues.size()) {
                return this.start + this.preGeneratedValues.get(this.preGeneratedValues.size() - 1);
            } else {
                return this.start + this.preGeneratedValues.get(this.index + 1);
            }
        }
        return this.start + this.minIncrement;
    }

    @Override
    public boolean allowIntegersOnly() {
        return true;
    }
}
