package org.ead.generate.types;

/**
 * Class for a simple nominal data type.
 * This class will generate the same value every time.
 * @author Markus Ullrich
 */
public class SimpleNominalDataType extends AbstractGenerateDataType {

    private String value;

    /**
     * Creates a new {@link SimpleNominalDataType}
     * @param name - the name of the data type
     * @param value - the value of the data type
     */
    public SimpleNominalDataType(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public String generateValue() {
        return this.value;
    }

    @Override
    public String getDefaultValue() {
        return this.value;
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
