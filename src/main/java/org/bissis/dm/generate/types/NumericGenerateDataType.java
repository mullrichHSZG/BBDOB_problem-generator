/**
 * 
 */
package org.bissis.dm.generate.types;

import org.bissis.dm.generate.data.CompressionList;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Class for numeric data types with a specific range.
 * Supports integer and floating point decimal types.
 * @author Markus Ullrich
 *
 */
public class NumericGenerateDataType extends NumericDatatype {

	private double lowerBound, upperBound, defaultValue;
	private CompressionList<Double> compressedValues;
	private Random random;
	private DecimalFormat format;
	private boolean useValueList;
	private boolean integerMode;
	private List<Double> valueList;
	private int valueIndex;

	private String lastValue;
	private boolean useLastValue;

	private int reShuffleCounter = 0;
	
	/**
	 * Creates a new NumericGenerateDataType object with the given lower and upper bounds. The lower bound must be smaller than the upper bound.
	 * @param name - The name of this data type.
	 * @param lowerBound - The lowest possible number for this data type.
	 * @param upperBound - The highest possible number for this data type.
	 * @param integerMode - Determines whether this data type supports integers only (true) or floating point decimals as well (false)
	 * @param defaultValue - the default value for this data type.
	 * @param useAllValues - if set to true, the generator will ensure that all possible integer values within the boundaries will be generated.
	 * @param rowCount - The number of rows that have to be generated.
	 * @throws IllegalArgumentException - if the lower bound is greater than the upper bound or if the number of rows is lower than all possible integer values within the boundaries and useAllValues is set to true.
	 */
	public NumericGenerateDataType(String name, double lowerBound, double upperBound, boolean integerMode, double defaultValue, boolean useAllValues, long rowCount) {
		super(name);
		this.compressedValues = new CompressionList<>();
		if(lowerBound > upperBound){
			throw new IllegalArgumentException("The lower bound must not be greater than the upper bound.");
		}
		if(defaultValue < lowerBound || defaultValue > upperBound){
			throw new IllegalArgumentException("The default value must be in between the lower and upper bound.");
		}
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.defaultValue = defaultValue;
		this.random = new Random();
		this.integerMode = integerMode;
		if(integerMode){
			this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
			this.format.applyPattern("0");
		}else{
			this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
			this.format.applyPattern("0.00");
		}
		this.valueList = new LinkedList<>();
		if (useAllValues) {
			if (this.getTotalDistinctValues() > rowCount) {
				throw new IllegalArgumentException("If the number of distinct values is higher than the number of rows, the use_all_values flag cannot be set to true!");
			}
			double i = this.lowerBound;
			while (i <= this.getTotalUpperBound()) {
				this.valueList.add(i);
				i++;
			}
			while (this.valueList.size() < rowCount) {
				this.valueList.add(this.generateNumericValue());
			}
			Collections.shuffle(this.valueList, this.random);
		}
		this.useValueList = useAllValues;
		this.valueIndex = 0;
	}

	/**
	 * Creates a new NumericGenerateDataType object with the given lower and upper bounds. The lower bound must be smaller than the upper bound.
	 * @param name - The name of this data type.
	 * @param lowerBound - The lowest possible number for this data type.
	 * @param upperBound - The highest possible number for this data type.
	 * @param integerMode - Determines whether this data type supports integers only (true) or floating point decimals as well (false)
	 * @param defaultValue - the default value for this data type.
	 * @param random - the random number generator that should be used from this instance.
	 * @param useAllValues - if set to true, the generator will ensure that all possible integer values within the boundaries will be generated.
	 * @param rowCount - The number of rows that have to be generated.
	 * @throws IllegalArgumentException - if the lower bound is greater than the upper bound or if the number of rows is lower than all possible integer values within the boundaries and useAllValues is set to true.
	 */
	public NumericGenerateDataType(String name, double lowerBound, double upperBound, boolean integerMode, double defaultValue, Random random, boolean useAllValues, long rowCount) {
		super(name);
		this.compressedValues = new CompressionList<>();
		if(lowerBound > upperBound){
			throw new IllegalArgumentException("The lower bound must not be greater than the upper bound.");
		}
		if(defaultValue < lowerBound || defaultValue > upperBound){
			throw new IllegalArgumentException("The default value must be in between the lower and upper bound.");
		}
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.defaultValue = defaultValue;
		this.random = random;
		this.integerMode = integerMode;
		if(integerMode){
			this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
			this.format.applyPattern("0");
		}else{
			this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
			this.format.applyPattern("0.00");
		}
		this.valueList = new LinkedList<>();
		if (useAllValues) {
			double i = this.lowerBound;
			while (i <= this.getTotalUpperBound()) {
				this.valueList.add(i);
				i++;
			}
			while (this.valueList.size() < rowCount) {
				this.valueList.add(this.generateNumericValue());
			}
			Collections.shuffle(this.valueList, this.random);
		}
		this.useValueList = useAllValues;
		this.valueIndex = 0;
	}

	/**
	 * Sets a new lower bound which must be smaller than or equal to the current upper bound.
	 * @param lowerBound the lowerBound to set
	 * @throws IllegalArgumentException - if the lower bound is greater than the current upper bound
	 * @throws MethodNotAllowedException - if useAllValues has been set to true during the creation of the generator.
	 */
	public void setLowerBound(double lowerBound) throws MethodNotAllowedException {
		if(this.useValueList) {
			throw new MethodNotAllowedException("Cannot change boundaries when useAllValues has been set to true.");
		}
		if(lowerBound > this.upperBound){
			throw new IllegalArgumentException("The lower bound must not be greater than the upper bound.");
		}
		this.lowerBound = lowerBound;
	}

	/**
	 * Sets a new upper bound which must be greater than or equal to the current lower bound.
	 * @param upperBound the upperBound to set
	 * @throws IllegalArgumentException - if the higher bound is smaller than the current lower bound
	 * @throws MethodNotAllowedException - if useAllValues has been set to true during the creation of the generator.
	 */
	public void setUpperBound(double upperBound) throws MethodNotAllowedException {
		if(this.useValueList) {
			throw new MethodNotAllowedException("Cannot change boundaries when useAllValues has been set to true.");
		}
		if(upperBound < this.lowerBound){
			throw new IllegalArgumentException("The upper bound must not be smaller than the lower bound.");
		}
		this.upperBound = upperBound;
	}

	/* (non-Javadoc)
	 * @see org.bissis.dm.generate.types.IGenerateDataType#generateValue()
	 */
	@Override
	public String generateValue() {
		if (useValueList) {
			//the value list ignores useLastValue completely
			return format.format(this.valueList.get(this.valueIndex++));
		}
		if (useLastValue) {
			useLastValue = false;
			return lastValue;
		}
		double randomNumber = this.random.nextDouble();
		double value = (upperBound - lowerBound) * randomNumber + lowerBound;
		this.compressedValues.addValue(value);
		this.lastValue = format.format(value);
		return lastValue;
	}

	@Override
	public String getDefaultValue() {
		return format.format(this.defaultValue);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NumericGenerateDataType [lowerBound=" + lowerBound + ", upperBound="
				+ upperBound + ", defaultValue=" + defaultValue + "]";
	}

	public CompressionList<Double> getCompressedValues() {
		return this.compressedValues;
	}

	@Override
	public double getTotalUpperBound() {
		return this.upperBound;
	}

	@Override
	public double getTotalLowerBound() {
		return this.lowerBound;
	}

	@Override
	public double getNextUpperBound() {
		return getTotalUpperBound();
	}

	@Override
	public double getNextLowerBound() {
		return getTotalLowerBound();
	}

	@Override
	public boolean allowIntegersOnly() {
		return this.integerMode;
	}

	@Override
	public void resetLastValue() {
		this.valueIndex--;
		reShuffleCounter++;
		if (reShuffleCounter >= 100) {
			reShuffleCounter = 0;
			//possibly too many failed attempts using the next value from the pre-generated list
			//in case a list is used, the remaining values will be re-shuffled
			if (!this.valueList.isEmpty()) {
				List<Double> replacementList = new ArrayList<>();
				for (int i = valueIndex ; i < this.valueList.size() ; i++){
					replacementList.add(this.valueList.get(i));
				}
				Collections.shuffle(replacementList, this.random);
				this.valueList = replacementList;
				this.valueIndex = 0;
			}
		}
	}

	@Override
	public long getTotalDistinctValues() {
		if (this.integerMode) {
			return (long) (this.upperBound - this.lowerBound + 1);
		} else {
			return Long.MAX_VALUE;
		}
	}

	@Override
	public long getNextDistinctValues() {
		return this.getTotalDistinctValues();
	}
}
