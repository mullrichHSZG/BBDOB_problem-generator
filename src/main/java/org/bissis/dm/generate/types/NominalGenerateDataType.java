/**
 * 
 */
package org.bissis.dm.generate.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for nominal data types.
 * @author Markus Ullrich
 *
 */
public class NominalGenerateDataType extends AbstractGenerateDataType {


	private List<String> options;
	private String defaultValue;
	private Random random;

	private String lastValue;
	private boolean useLastValue;
	
	/**
	 * Creates a new NominalGenerateDataType object without any options.
	 * NOTE: The generateValue() method throws an IllegalStateException if it is called with no options to choose from.
	 * 		 You need to add options with the addOption() method first.
	 */
	public NominalGenerateDataType(String typeName) {
		super(typeName);
		this.options = new ArrayList<String>();
		this.defaultValue = null;
		this.random = new Random();
	}

	/**
	 * Adds another option for this data type to choose from when generating a value.
	 * @param option - the new option
	 */
	public void addOption(String option, boolean defaultValue) {
		this.options.add(option);
		if(defaultValue){
			this.defaultValue = option;
		}
	}

	/**
	 * Removes an existing option from this data type.
	 * If two or more options exist that are equal to each other, only the first one is removed.
	 * @param option - the option which should be removed
	 * @return true if an option with the specified name could be found and removed, false otherwise
	 */
	public boolean removeOption(String option) {
		if(option.equals(this.defaultValue)){
			this.defaultValue = null;
		}
		return this.options.remove(option);
	}

	/* (non-Javadoc)
	 * @see org.bissis.dm.generate.types.IGenerateDataType#generateValue()
	 */
	@Override
	public String generateValue() {
		if(options.isEmpty()){
			throw new IllegalStateException("No options have been added yet.");
		}
		if (useLastValue) {
			useLastValue = false;
			return lastValue;
		}
		int randomNumber = this.random.nextInt(this.options.size());
		this.lastValue = this.options.get(randomNumber);
		return this.lastValue;
	}

	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String optionString = "";
		for(String option : options){
			optionString += option + ", ";
		}
		optionString = optionString.substring(0, optionString.length() - 2);
		return "NominalGenerateDataType [options=" + optionString + "; the default value is "
				+ defaultValue + "]";
	}

	@Override
	public void resetLastValue() {
		this.useLastValue = true;
	}

	@Override
	public long getTotalDistinctValues() {
		return this.options.size();
	}

	@Override
	public long getNextDistinctValues() {
		return this.options.size();
	}

}
