package org.bissis.dm.generate.types;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

/**
 * Class for a numeric data type which generates normal distributed values instead of equally distributed values.
 * Mean and standard deviation are configurable and also determine the upper and lower bound of the generated values.
 * For now the default is mean +/- 3 * standard deviation. This should help to reduce the probability of long waiting
 * intervals during the generation process. Future versions might allow the configuration of these boundaries.
 * @author Markus Ullrich
 */
public class GaussianDistributionDataType extends NumericDataType {

    private double mean, standardDeviation;
    private Random random;
    private boolean integerMode;

    private String lastValue;
    private boolean useLastValue;

    private DecimalFormat format;

    /**
     * Generates a new {@link GaussianDistributionDataType}.
     * @param name - the name of this data type
     * @param mean - the mean value, also used as default
     * @param standardDeviation - the standard deviation for this distribution
     * @param integerMode - enforces the generation of integer values only
     */
    @SuppressWarnings("unused")
    public GaussianDistributionDataType(String name, double mean, double standardDeviation, boolean integerMode) {
        super(name);
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.random = new Random();
        this.integerMode = integerMode;
        if(integerMode){
            this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
            this.format.applyPattern("0");
        }else{
            this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
            this.format.applyPattern("0.00");
        }
    }

    /**
     * Generates a new {@link GaussianDistributionDataType}.
     * @param name - the name of this data type
     * @param mean - the mean value, also used as default
     * @param standardDeviation - the standard deviation for this distribution
     * @param random - the random number generator that should be used by this data type
     * @param integerMode - enforces the generation of integer values only
     */
    public GaussianDistributionDataType(String name, double mean, double standardDeviation, Random random, boolean integerMode) {
        super(name);
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.random = random;
        this.integerMode = integerMode;
        if(integerMode){
            this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
            this.format.applyPattern("0");
        }else{
            this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
            this.format.applyPattern("0.00");
        }
    }

    @Override
    public String generateValue() {
        if (useLastValue){
            useLastValue = false;
            return lastValue;
        }
        lastValue = format.format(standardDeviation * random.nextGaussian() + mean);
        return lastValue;
    }

    @Override
    public String getDefaultValue() {
        return format.format(mean);
    }

    @Override
    public void resetLastValue() {
        this.useLastValue = true;
    }

    @Override
    public long getTotalDistinctValues() {
        if (this.integerMode) {
            return (int)standardDeviation * 6 + 1;
        }
        return Long.MAX_VALUE;
    }

    @Override
    public long getNextDistinctValues() {
        return getTotalDistinctValues();
    }

    @Override
    public double getTotalUpperBound() {
        if (this.integerMode) {
            return Math.floor(mean + standardDeviation * 3);
        }
        return mean + standardDeviation * 3;
    }

    @Override
    public double getTotalLowerBound() {
        if (this.integerMode) {
            return Math.floor(mean - standardDeviation * 3);
        }
        return mean  - standardDeviation * 3;
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
        return this.integerMode;
    }
}
