package org.bissis.dm.generate.types;

/**
 * The interface for every data type.
 * Created by Markus Ullrich on 08.10.2015.
 */
public interface IGenerateDataType extends Comparable<String>{

    /**
     * Generates the next value from this data type and returns it in String format.
     * @return the next generated value
     */
    public String generateValue();

    /**
     * Returns the default value for this data type.
     * Generators with pre-generated next values (e.g. ID types) will return the next value as their current default.
     * @return the default value
     */
    public String getDefaultValue();

    /**
     * Returns the nme of this data type.
     * @return the name
     */
    public String getName();

    /**
     * After this method has been called, the generator has to ensure that the next generated value is equal to or the same than the previous generated value.
     */
    public void resetLastValue();

    /**
     * Returns the total number of distinct values that can be created by this generator.
     * @return
     */
    long getTotalDistinctValues();

    /**
    * Returns the number of distinct values that can be created next by this generator.
    * @return
    */
    long getNextDistinctValues();

}
