package org.bissis.dm.generate.types;

/**
 * Class for a simple nominal data type.
 * This class will generate the same value every time.
 * Created by bissi on 22.03.2018.
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
