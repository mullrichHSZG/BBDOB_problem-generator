package org.bissis.dm.generate.types;

/**
 * Created by bissi on 13.03.2018.
 */
public class NaNGenerator extends NumericDatatype {

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
