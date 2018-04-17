/**
 * 
 */
package org.bissis.dm.generate.types;

import org.bissis.dm.utility.ValueCompression;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Class for creating a number of data types that use six different compression methods:
 * min, max, mean, median, 25percentile and 75percentile
 * That prevents the user from having to create a single attribute for each compression type and to create constraints like attribute_min <= attribute_25percentile
 * Allows also to easily create six additional data types that generate higher values for all of the compression methods most of the time (overall prefix) and another data type which generated values will always be at least as high as the other values (available prefix).
 * 
 * Values need to be generated in generation cycles, which means that each of the generators that are used, need to be called once (and only once) before the next value can be created
 * @author Markus Ullrich
 *
 */
@Deprecated
@SuppressWarnings({"unused", "deprecation", "WeakerAccess"})
public class CompressionDataType extends AbstractGenerateDataType {

	private static final Map<String,Map<String,String>> valuePool = new HashMap<>();
	private static final int baseValueCount = 50;
	
	private static final String OVERALL_PREFIX = "OVERALL_";
	private static final String AVAILABLE_PREFIX = "AVAILABLE_";
	
	private static final String MIN_SUFFIX = "_MIN";
	private static final String MAX_SUFFIX = "_MAX";
	private static final String MEAN_SUFFIX = "_MEAN";
	private static final String MEDIAN_SUFFIX = "_MEDIAN";
	private static final String THIRD_QUARTILE_SUFFIX = "_75PERCENTILE";
	private static final String FIRST_QUARTILE_SUFFIX = "_25PERCENTILE";
	
	private String attributeBaseName, attributeName;
	private double lowerBound, upperBound, realUpperBound;
	private Random random;
	private DecimalFormat format;
	private boolean useRandomUpperBound;
	private double upperBoundOverallModificationFactor;
	private boolean integerMode;
	
	/**
	 * Creates a new CompressionDatatype
	 * @param attributeBaseName - the attribute name without a compression method suffix or the overall and available prefix
	 * @param attributeName - the name with a certain suffix for the used compression method and in some cases an additional prefix
	 * @param lowerBound - the lowest value that can be generated by data types of this kind
	 * @param upperBound - the highest value that can be generated by data types of this kind
	 * @param upperBoundOverallModificationFactor - modification factor for the overall prefixed attributes to allow much higher values for them
	 * @param integerMode - determines whether the generated values are integers (true) or floating point decimals (false)
	 * @param useRandomUpperBound - if set to true, the upper bound will be recalculated each cycle as a value between the lower bound and the upper bound 
	 */
	private CompressionDataType(String attributeBaseName, String attributeName, double lowerBound, double upperBound, double upperBoundOverallModificationFactor, boolean integerMode, boolean useRandomUpperBound){
		super(attributeBaseName);
		this.attributeBaseName = attributeBaseName;
		this.attributeName = attributeName;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.realUpperBound = upperBound;
		this.useRandomUpperBound = useRandomUpperBound;
		this.integerMode = integerMode;
		this.upperBoundOverallModificationFactor = upperBoundOverallModificationFactor;
		this.random = new Random();
		if(integerMode){
			this.format = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.US);
			this.format.applyPattern("0");
		}else{
			this.format = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
			this.format.applyPattern("0.00");
		}
	}
	
	/**
	 * Get the generated attribute name for this CompressionDatatype instance.
	 * @return - the attribute name
	 */
	public String getAttributeName(){
		return this.attributeName;
	}
	
	/**
	 * Creates a new list with six, twelve or thirteen CompressionDatatype instances based on the given values.
	 * @param attributeBaseName - The name of the attribute. Each generated data type will have a new name consisting of the original attribute name, a compression method suffix and an optional overall or available prefix.
	 * @param lowerBound - The lowest value that can be generated by each of the data types.
	 * @param upperBound - the highest value that can be generated by each of the 'normal' data types. Data types with an additional prefix could generate higher values if the upperBoundOverallModificationFactor is set to more than 1.0.
	 * @param upperBoundOverallModificationFactor - determines how much larger the generated values by the prefixed data types can be compared to the 'normal' data types.
	 * @param integerMode - determines whether the generated values are integers (true) or floating point decimals (false)
	 * @param useOverallPrefix - if set to true, six additional data types are generated with the overall prefix, the generated values of those will be higher than the ones generated by the 'normal' data types most of the time 
	 * @param useRandomUpperBound - if set to true, the upper bound will be recalculated each cycle as a value between the lower bound and the upper bound
	 * @param useUpperBoundAsAvailable - if set to true, an additional data type will be generated which creates the highest value guaranteed each generation cycle
	 * @return a list with several data types to generate compressed values along with a single numeric data type
	 */
	public static List<CompressionDataType> createDatatypeList(String attributeBaseName, double lowerBound, double upperBound, double upperBoundOverallModificationFactor, boolean integerMode, boolean useOverallPrefix, boolean useRandomUpperBound, boolean useUpperBoundAsAvailable){
		List<CompressionDataType> datatypeList = new ArrayList<>();
		CompressionDataType.valuePool.put(attributeBaseName, new HashMap<String,String>());
		datatypeList.add(new CompressionDataType(attributeBaseName, attributeBaseName + CompressionDataType.MEAN_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		datatypeList.add(new CompressionDataType(attributeBaseName, attributeBaseName + CompressionDataType.MEDIAN_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		datatypeList.add(new CompressionDataType(attributeBaseName, attributeBaseName + CompressionDataType.MIN_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		datatypeList.add(new CompressionDataType(attributeBaseName, attributeBaseName + CompressionDataType.MAX_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		datatypeList.add(new CompressionDataType(attributeBaseName, attributeBaseName + CompressionDataType.THIRD_QUARTILE_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		datatypeList.add(new CompressionDataType(attributeBaseName, attributeBaseName + CompressionDataType.FIRST_QUARTILE_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		if(useUpperBoundAsAvailable){
			datatypeList.add(new CompressionDataType(attributeBaseName, CompressionDataType.AVAILABLE_PREFIX + attributeBaseName, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		}
		if(useOverallPrefix){
			String overallAttributeBaseName = CompressionDataType.OVERALL_PREFIX + attributeBaseName;
			CompressionDataType.valuePool.put(overallAttributeBaseName, new HashMap<String,String>());
			datatypeList.add(new CompressionDataType(attributeBaseName, overallAttributeBaseName + CompressionDataType.MEAN_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
			datatypeList.add(new CompressionDataType(attributeBaseName, overallAttributeBaseName + CompressionDataType.MEDIAN_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
			datatypeList.add(new CompressionDataType(attributeBaseName, overallAttributeBaseName + CompressionDataType.MIN_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
			datatypeList.add(new CompressionDataType(attributeBaseName, overallAttributeBaseName + CompressionDataType.MAX_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
			datatypeList.add(new CompressionDataType(attributeBaseName, overallAttributeBaseName + CompressionDataType.THIRD_QUARTILE_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
			datatypeList.add(new CompressionDataType(attributeBaseName, overallAttributeBaseName + CompressionDataType.FIRST_QUARTILE_SUFFIX, lowerBound, upperBound, upperBoundOverallModificationFactor, integerMode, useRandomUpperBound));
		}
		return datatypeList;
	}
	
	/**
	 * Generates the new values for the next generation cycle and stores them in the valuePool. This method is called automatically if the generateValue() method cannot find the value from the current cycle in the valuePool.
	 * The generateValue() method then just returns the corresponding value in the valuePool by the attribute name and removes the value from the pool in the process.
	 */
	private void generateNewValues(){
		int randomBaseValueCount = (int) Math.round((double)(baseValueCount+1) * random.nextDouble()) + 1;
		double[] baseValues = new double[randomBaseValueCount];
		double[] overallBaseValues = new double[randomBaseValueCount];
		if(useRandomUpperBound){
			double randomNumber = random.nextDouble();
			this.upperBound = (realUpperBound - lowerBound) * randomNumber + lowerBound;
		}
		for(int i = 0 ; i < randomBaseValueCount ; i++){
			double randomNumber = this.random.nextDouble() * 0.8;
			double value = (upperBound - lowerBound) * randomNumber + lowerBound;
			double overallValue = (upperBound * upperBoundOverallModificationFactor - lowerBound) * randomNumber + lowerBound;
			baseValues[i] = value;
			overallBaseValues[i] = Math.min(Math.max(value * 1.2, overallValue), this.upperBound * upperBoundOverallModificationFactor);
		}
		double median = ValueCompression.getMedian(baseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(this.attributeBaseName + CompressionDataType.MEDIAN_SUFFIX, this.format.format(median));
		double mean = ValueCompression.getMean(baseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(this.attributeBaseName + CompressionDataType.MEAN_SUFFIX, this.format.format(mean));
		double min = ValueCompression.getMin(baseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(this.attributeBaseName + CompressionDataType.MIN_SUFFIX, this.format.format(min));
		double max = ValueCompression.getMax(baseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(this.attributeBaseName + CompressionDataType.MAX_SUFFIX, this.format.format(max));
		double firstQuartile = ValueCompression.get25Percentile(baseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(this.attributeBaseName + CompressionDataType.FIRST_QUARTILE_SUFFIX, this.format.format(firstQuartile));
		double thirdQuartile = ValueCompression.get75Percentile(baseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(this.attributeBaseName + CompressionDataType.THIRD_QUARTILE_SUFFIX, this.format.format(thirdQuartile));
		
		double overallMedian = ValueCompression.getMedian(overallBaseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(OVERALL_PREFIX + this.attributeBaseName + CompressionDataType.MEDIAN_SUFFIX, this.format.format(overallMedian));
		double overallMean = ValueCompression.getMean(overallBaseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(OVERALL_PREFIX + this.attributeBaseName + CompressionDataType.MEAN_SUFFIX, this.format.format(overallMean));
		double overallMin = ValueCompression.getMin(overallBaseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(OVERALL_PREFIX + this.attributeBaseName + CompressionDataType.MIN_SUFFIX, this.format.format(overallMin));
		double overallMax = ValueCompression.getMax(overallBaseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(OVERALL_PREFIX + this.attributeBaseName + CompressionDataType.MAX_SUFFIX, this.format.format(overallMax));
		double overallFirstQuartile = ValueCompression.get25Percentile(overallBaseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(OVERALL_PREFIX + this.attributeBaseName + CompressionDataType.FIRST_QUARTILE_SUFFIX, this.format.format(overallFirstQuartile));
		double overallThirdQuartile = ValueCompression.get75Percentile(overallBaseValues);
		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(OVERALL_PREFIX + this.attributeBaseName + CompressionDataType.THIRD_QUARTILE_SUFFIX, this.format.format(overallThirdQuartile));

		CompressionDataType.valuePool.get(this.attributeBaseName)
			.put(AVAILABLE_PREFIX + this.attributeBaseName, this.format.format(this.upperBound * this.upperBoundOverallModificationFactor));
	}
	
	/* (non-Javadoc)
	 * @see org.bissis.dm.generate.types..IGenerateDataType#generateValue()
	 */
	public String generateValue() {
		if(!CompressionDataType.valuePool.get(attributeBaseName).containsKey(attributeName)){
			this.generateNewValues();
		}
		return CompressionDataType.valuePool.get(attributeBaseName).remove(attributeName);
	}

	public String getDefaultValue() {
		return CompressionDataType.valuePool.get(attributeBaseName).get(attributeName);
	}

	@Override
	public void resetLastValue() {
		//Since this class is deprecated, this method has not been implemented.
	}

	@Override
	public long getTotalDistinctValues() {
		if (this.integerMode) {
			return (long) upperBound - (long) lowerBound;
		}
		return Long.MAX_VALUE;
	}

	@Override
	public long getNextDistinctValues() {
		return getTotalDistinctValues();
	}

}
