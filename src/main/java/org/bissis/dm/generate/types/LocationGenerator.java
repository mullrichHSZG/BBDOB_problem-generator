package org.bissis.dm.generate.types;

/**
 * Created by bissi on 26.11.2017.
 */
public class LocationGenerator extends AbstractGenerateDataType {

    //TODO: remove or turn this into something
    public LocationGenerator() {
        super("");
    }

    public String generateValue() {
        return null;
    }


    public String getDefaultValue() {
        return null;
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
