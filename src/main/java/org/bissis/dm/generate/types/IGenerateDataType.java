package org.bissis.dm.generate.types;

/**
 * The interface for every data type.
 * @author Markus Ullrich
 */
public interface IGenerateDataType extends Comparable<String>{

    /**
     * Generates the next value from this data type and returns it in String format.
     * @return the next generated value
     */
    String generateValue();

    /**
     * Returns the default value for this data type.
     * Generators with pre-generated next values (e.g. ID types) will return the next value as their current default.
     * @return the default value
     */
    String getDefaultValue();

    /**
     * Returns the nme of this data type.
     * @return the name
     */
    String getName();

    /**
     * After this method has been called, the generator has to ensure that the next generated value is equal to or the same than the previous generated value.
     */
    void resetLastValue();

    /**
     * Returns the total number of distinct values that can be created by this generator.
     * @return the number of total distinct values
     */
    long getTotalDistinctValues();

    /**
    * Returns the number of distinct values that can be created next by this generator.
    * @return the number of distinct values
    */
    long getNextDistinctValues();

}
