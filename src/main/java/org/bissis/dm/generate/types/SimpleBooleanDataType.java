package org.bissis.dm.generate.types;

/**
 * Class for a simple boolean data type.
 * This class will generate the same value every time.
 * Created by bissi on 22.03.2018.
 */
public class SimpleBooleanDataType extends AbstractGenerateDataType {

    private boolean value;

    /**
     * Creates a new {@link SimpleBooleanDataType}
     * @param name - the name of the data type
     * @param value - the value of the data type
     */
    public SimpleBooleanDataType(String name, boolean value) {
        super(name);
        this.value = value;
    }

    @Override
    public String generateValue() {
        return "" + this.value;
    }

    @Override
    public String getDefaultValue() {
        return "" + this.value;
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
