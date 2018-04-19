package org.ead.generate.types;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Class for a simple numeric data type.
 * This class will generate the same value every time.
 * @author Markus Ullrich
 */
public class SimpleNumberDataType extends NumericDataType {

    private double number;
    private boolean integer;

    private DecimalFormat format;

    /**
     * Creates a new {@link SimpleNumberDataType}
     * @param name - the name of the data type
     * @param number - the numeric value of the data type
     * @param integer - if set to true, the value will be formatted as integer
     */
    public SimpleNumberDataType(String name, double number, boolean integer) {
        super(name);
        this.number = number;
        this.integer = integer;
        if(integer){
            this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
            this.format.applyPattern("0");
        }else{
            this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
            this.format.applyPattern("0.00");
        }
    }

    @Override
    public String generateValue() {
        return getDefaultValue();
    }

    @Override
    public String getDefaultValue() {
        return format.format(number);
    }

    @Override
    public double getTotalUpperBound() {
        return number;
    }

    @Override
    public double getTotalLowerBound() {
        return number;
    }

    @Override
    public double getNextUpperBound() {
        return number;
    }

    @Override
    public double getNextLowerBound() {
        return number;
    }

    @Override
    public boolean allowIntegersOnly() {
        return this.integer;
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
