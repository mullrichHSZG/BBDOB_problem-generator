/**
 * 
 */
package org.bissis.dm.utility;

import java.util.Arrays;

/**
 * Class with static methods for compressing double values in different ways.
 * @author Markus Ullrich
 *
 */
public class ValueCompression {

	/**
	 * Compresses the given floating point decimals values into the mean value.
	 * @param values - the values to compress
	 * @return The mean value.
	 */
	public static double getMean(double[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double sum = 0.0;
		for(double value : values){
			sum += value;
		}
		return sum / (double)values.length;
	}
	
	/**
	 * Compresses the given integer values into the mean value.
	 * @param values - the values to compress
	 * @return The mean value as a floating point decimal.
	 */
	public static double getMean(int[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double sum = 0.0;
		for(double value : values){
			sum += value;
		}
		return sum / (double)values.length;
	}
	
	/**
	 * Compresses the given floating point decimal values into the median value.
	 * @param values - the given values
	 * @return The median.
	 */
	public static double getMedian(double[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double[] sortedValues = Arrays.copyOf(values, values.length);
		Arrays.sort(sortedValues);
		if(values.length % 2 == 0){
			return (sortedValues[values.length/2-1] + sortedValues[values.length/2]) / 2;
		}
		return sortedValues[(int) Math.round(values.length / 2 - 0.5)];
	}
	
	/**
	 * Compresses the given integer values into the median value.
	 * @param values - the given values
	 * @return The median as a floating point decimal.
	 */
	public static double getMedian(int[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		int[] sortedValues = Arrays.copyOf(values, values.length);
		Arrays.sort(sortedValues);
		if(values.length % 2 == 0){
			return ((double)sortedValues[values.length/2-1] + (double)sortedValues[values.length/2]) / 2.0;
		}
		return sortedValues[(int) Math.round(values.length / 2 - 0.5)];
	}
	
	/**
	 * Compresses the given floating point decimal values into the minimum value.
	 * @param values - the given values
	 * @return The minimum value.
	 */
	public static double getMin(double[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double min = Double.MAX_VALUE;
		for(double value : values){
			if(value < min){
				min = value;
			}
		}
		return min;
	}
	
	/**
	 * Compresses the given integer values into the minimum value.
	 * @param values - the given values
	 * @return The minimum value.
	 */
	public static int getMin(int[] values){
		if(values == null){
			return 0;
		}
		if(values.length == 0){
			return 0;
		}
		int min = Integer.MAX_VALUE;
		for(int value : values){
			if(value < min){
				min = value;
			}
		}
		return min;
	}
	
	/**
	 * Compresses the given floating point decimal values into the first quartile value.
	 * @param values - the given values
	 * @return The first quartile value.
	 */
	public static double get25Percentile(double[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double[] sortedValues = Arrays.copyOf(values, values.length);
		Arrays.sort(sortedValues);
		int index = Math.round(0.25f * values.length - 0.5f);
		return sortedValues[index];
	}
	
	/**
	 * Compresses the given integer values into the first quartile value.
	 * @param values - the given values
	 * @return The first quartile value.
	 */
	public static int get25Percentile(int[] values){
		if(values == null){
			return 0;
		}
		if(values.length == 0){
			return 0;
		}
		int[] sortedValues = Arrays.copyOf(values, values.length);
		Arrays.sort(sortedValues);
		int index = Math.round(0.25f * values.length - 0.5f);
		return sortedValues[index];
	}
	
	/**
	 * Compresses the given floating point decimal values into the third quartile value.
	 * @param values - the given values
	 * @return The third quartile value.
	 */
	public static double get75Percentile(double[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double[] sortedValues = Arrays.copyOf(values, values.length);
		Arrays.sort(sortedValues);
		int index = Math.round(0.75f * values.length - 0.6f);
		return sortedValues[index];
	}
	
	/**
	 * Compresses the given integer values into the third quartile value.
	 * @param values - the given values
	 * @return The third quartile value.
	 */
	public static int get75Percentile(int[] values){
		if(values == null){
			return 0;
		}
		if(values.length == 0){
			return 0;
		}
		int[] sortedValues = Arrays.copyOf(values, values.length);
		Arrays.sort(sortedValues);
		int index = Math.round(0.75f * values.length - 0.6f);
		return sortedValues[index];
	}
	
	/**
	 * Compresses the given floating point decimal values into the maximum value.
	 * @param values - the given values
	 * @return The maximum value.
	 */
	public static double getMax(double[] values){
		if(values == null){
			return 0.0;
		}
		if(values.length == 0){
			return 0.0;
		}
		double max = -Double.MAX_VALUE;
		for(double value : values){
			if(value > max){
				max = value;
			}
		}
		return max;
	}
	
	/**
	 * Compresses the given integer values into the maximum value.
	 * @param values - the given values
	 * @return The maximum value.
	 */
	public static int getMax(int[] values){
		if(values == null){
			return 0;
		}
		if(values.length == 0){
			return 0;
		}
		int max = Integer.MIN_VALUE;
		for(int value : values){
			if(value > max){
				max = value;
			}
		}
		return max;
	}
	
}
