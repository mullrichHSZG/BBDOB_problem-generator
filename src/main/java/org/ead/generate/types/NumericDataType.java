/**
 * 
 */
package org.ead.generate.types;

/**
 * Class for numeric data types with a specific range.
 * Every type for which its values exists a numeric representation can extend this class instead of AbstractGenerateDtaType.
 * @author Markus Ullrich
 *
 */
public abstract class NumericDataType extends AbstractGenerateDataType {

	protected NumericDataType(String name) {
		super(name);
	}

	/**
	 * Generates the next value for this generator and converts it into a double.
	 * If your type extends from this class and your result is not a valid representation of a Double value,
	 * please override this method with an appropriate translation.
	 * @return the next generated value represented as double
     */
	public double generateNumericValue() {
		return Double.parseDouble(this.generateValue());
	}

	/**
	 * Returns the default for this generator and converts it into a double.
	 * If your type extends from this class and your result is not a valid representation of a Double value,
	 * please override this method with an appropriate translation.
	 * @return the default value represented as double
	 */
	public double defaultNumericValue() {
		return Double.parseDouble(this.getDefaultValue());
	}

	/**
	 * Returns the total upper bound for this numeric data type.
	 * @return the total upper bound.
     */
	public abstract double getTotalUpperBound();

	/**
	 * Returns the total lower bound for this numeric data type.
	 * @return the total lower bound.
	 */
	public abstract double getTotalLowerBound();

	/**
	 * Returns the upper bound for this numeric data type for the next value.
	 * Generators with pre-generated next values (e.g. ID types) have to return the upper bound for the value after that.
	 * @return the upper bound.
	 */
	public abstract double getNextUpperBound();

	/**
	 * Returns the lower bound for this numeric data type for the next value.
	 * Generators with pre-generated next values (e.g. ID types) have to return the lower bound for the value after that.
	 * @return the lower bound.
	 */
	public abstract double getNextLowerBound();

	/**
	 * Returns true if this data type generates integer values only.
	 * @return true if only integers are generated
     */
	public abstract boolean allowIntegersOnly();

}
