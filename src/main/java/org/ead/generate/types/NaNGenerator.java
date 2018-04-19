package org.ead.generate.types;

/**
 * Class for a generator that only generates Double.NaN values.
 * @author Markus Ullrich
 */
public class NaNGenerator extends NumericDataType {

    public NaNGenerator(String name) {
        super(name);
    }

    @Override
    public double getTotalUpperBound() {
        return Double.NaN;
    }

    @Override
    public double getTotalLowerBound() {
        return Double.NaN;
    }

    @Override
    public double getNextUpperBound() {
        return Double.NaN;
    }

    @Override
    public double getNextLowerBound() {
        return Double.NaN;
    }

    @Override
    public boolean allowIntegersOnly() {
        return false;
    }

    @Override
    public String generateValue() {
        return "" + Double.NaN;
    }

    @Override
    public String getDefaultValue() {
        return "" + Double.NaN;
    }

    @Override
    public void resetLastValue() {
        //nothing to do here
    }

    @Override
    public long getTotalDistinctValues() {
        return 1;
    }

    @Override
    public long getNextDistinctValues() {
        return 1;
    }
}
