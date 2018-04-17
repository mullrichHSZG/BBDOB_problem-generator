/**
 * 
 */
package org.bissis.dm.generate;

import org.bissis.dm.generate.data.ProblemGenerationAttribute;
import org.bissis.dm.generate.data.ProblemGenerationConstraint;
import org.bissis.dm.generate.data.ProblemGenerationParameter;
import org.bissis.dm.generate.expressions.IExpression;
import org.bissis.dm.generate.types.IGenerateDataType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Class for generating test data for data mining.
 * @author Markus Ullrich
 *
 */
public class TestDataGenerator {

	private String newline = System.getProperty("line.separator");
	private String valueSeparator = ";";

	private ProblemGenerationConfiguration config;

	public TestDataGenerator(ProblemGenerationConfiguration config) {
		this.config = config;
	}

	private boolean checkConstraints() {
		boolean constraintsMet = true;
		if (config.isNoDuplicates()) {
			long possibleDistinctRows = 1;
			for (ProblemGenerationAttribute attribute : this.config.getProblemGenerationAttributeList()) {
				if (attribute.isOutput())
					possibleDistinctRows *= attribute.getType().getTotalDistinctValues();
				if (possibleDistinctRows >= config.getNumberOfRows())
					break;
			}
			constraintsMet &= config.getNumberOfRows() <= possibleDistinctRows;
		}
		for (ProblemGenerationConstraint constraint : config.getProblemGenerationConstraintList()) {
			constraintsMet &= constraint.isPossible();
		}
		return constraintsMet;
	}

	private StringBuilder generateComments() {
		String commentPrefix = config.getCommentPrefix();
		StringBuilder commentString = new StringBuilder();
		commentString.append(commentPrefix + newline);
		commentString.append(commentPrefix + " Problem instance for: " + config.getProblemName() + newline);
		commentString.append(commentPrefix + newline);
		commentString.append(commentPrefix + " Created with ProblemGenerator version: " + config.getVersion() + newline);
		commentString.append(commentPrefix + newline);
		commentString.append(commentPrefix + " Java version used: " + System.getProperty("java.version") + newline);
		commentString.append(commentPrefix + newline);
		commentString.append(commentPrefix + " Global seed for randomness: " + config.getGlobalSeed() + newline);
		commentString.append(commentPrefix + newline);
		boolean additionalSeedsMentioned = false;
		for (ProblemGenerationAttribute attribute : config.getProblemGenerationAttributeList()) {
			if (attribute.getLocalSeed() != null) {
				if (!additionalSeedsMentioned) {
					additionalSeedsMentioned = true;
					commentString.append(commentPrefix + " The following local seeds have been used:" + newline);
				}
				commentString.append(commentPrefix + " " + attribute.getLocalSeed() + " for " + attribute.getType().getName() + newline);
			}
		}
		commentString.append(commentPrefix + newline);
		if (config.isPrintParameters()) {
			boolean parametersMentioned = false;
			for (ProblemGenerationParameter param : config.getGenerateParameterList()) {
				if (!parametersMentioned) {
					parametersMentioned = true;
					commentString.append(commentPrefix + " Problem Parameters:" + newline);
				}
				commentString.append(commentPrefix + " " + param.getName() + ": " + param.getDataType().getDefaultValue() + newline);
			}
			commentString.append(commentPrefix + newline);
		}
		if (!config.getComments().isEmpty()) {
			commentString.append(commentPrefix + " Additional comments:" + newline);
			for (String comment : config.getComments()) {
				commentString.append(commentPrefix + " " + comment + newline);
			}
			commentString.append(commentPrefix + newline);
		}
		return commentString;
	}

	public void generateAndWriteToFile(File testDataFile) throws IOException {
		config.getProblemData().setConfiguration(config);
		this.valueSeparator = config.getSeparator();
		IGenerateDataType[] order = this.orderTypesByConstraints();
		if (!this.checkConstraints()) {
			throw new IllegalStateException("Some constraints cannot be met, please check the configuration and try again.");
		}
		//TODO: RegExp support

		if(!testDataFile.exists()){
			/*if(!testDataFile.getParentFile().exists()){
				testDataFile.getParentFile().mkdirs();
			}*/
			testDataFile.createNewFile();
		}
		StringBuffer outputString = new StringBuffer();
		outputString.append(this.generateComments());

		if (config.getAlternativeHeader() == null) {
			for (int index = 0; index < config.getProblemGenerationAttributeList().size(); index++) {
				ProblemGenerationAttribute attribute = config.getProblemGenerationAttributeList().get(index);
				if (attribute.isOutput())
					outputString.append(attribute.getType().getName() + valueSeparator);
			}
			outputString.replace(outputString.length() - 1, outputString.length(), "");
		} else {
			outputString.append(config.getAlternativeHeader());
		}
		outputString.append(this.newline);

		for (int row = 0 ; row < config.getNumberOfRows() ; row++) {
			config.getProblemData().generateData(config.getProblemGenerationConstraintList(), order);
			for (int index = 0 ; index < config.getProblemGenerationAttributeList().size() ; index++) {
				ProblemGenerationAttribute attribute = config.getProblemGenerationAttributeList().get(index);
				if(attribute.isOutput())
					if (attribute.isOutputProbabilitySet() && config.getGlobalRandom().nextDouble() > attribute.getOutputProbability()) {
						//Do nothing
					} else {
						outputString.append(config.getProblemData().getLastValueFor(attribute.getType().getName()) + valueSeparator);
					}
			}
			outputString.replace(outputString.length() - 1, outputString.length(), "");
			outputString.append(this.newline);
		}

		FileWriter testDataFileWriter = new FileWriter(testDataFile);
		testDataFileWriter.write(outputString.toString());
		testDataFileWriter.flush();
		testDataFileWriter.close();
	}

	private IGenerateDataType[] orderTypesByConstraints() {
		IGenerateDataType[] order = new IGenerateDataType[config.getProblemData().getDataTypes().size()];
		List<IGenerateDataType> noIncomingEdges = new ArrayList<>();
		List<IGenerateDataType> incomingEdges = new ArrayList<>();
		List<ProblemGenerationConstraint> constraintCopyList = new ArrayList<>();
		for (ProblemGenerationConstraint constraint : config.getProblemGenerationConstraintList()) {
			constraintCopyList.add(constraint);
		}
		int index  = 0;
		for (ProblemGenerationAttribute attribute : config.getProblemGenerationAttributeList()) {
			boolean noLeftConstraints = true;
			for (ProblemGenerationConstraint constraint : config.getProblemGenerationConstraintList()) {
				if (attribute.getType().getName().equals(constraint.getLeft().getDataTypeName()) ||
						attribute.getType().getName().equals(constraint.getLeft().getSecondDataTypeName())) {
					//also check that the right side is not numbers only
					if ((constraint.getRight().getDataTypeName() == null ||
							config.getProblemData().getTypeForName(constraint.getRight().getDataTypeName()) == null) &&
								(constraint.getRight().getSecondDataTypeName() == null ||
									config.getProblemData().getTypeForName(constraint.getRight().getSecondDataTypeName()) == null)) {
						//All good
					} else {
						noLeftConstraints = false;
						incomingEdges.add(attribute.getType());
						break;
					}
				}
			}
			if (noLeftConstraints) {
				noIncomingEdges.add(attribute.getType());
			}
		}
		while(!noIncomingEdges.isEmpty()) {
			IGenerateDataType nextType = noIncomingEdges.remove(0);
			order[index] = nextType;
			index++;
			//find edge from nextType to other types
			List<IGenerateDataType> haveEdgeFromType = new ArrayList<>();
			List<ProblemGenerationConstraint> toBeRemoved = new ArrayList<>();
			for (ProblemGenerationConstraint constraint : constraintCopyList) {
				if (nextType.getName().equals(constraint.getRight().getDataTypeName())) {
					//found one
					if (constraint.getRight().getSecondDataTypeName() == null ||
							noIncomingEdges.contains(config.getProblemData().getTypeForName(constraint.getRight().getSecondDataTypeName()))) {
						//making sure we have seen and evaluated the second type before as well or that it does not exist
						this.addTypesFromExpressionNoDuplicates(haveEdgeFromType, constraint.getLeft());
						toBeRemoved.add(constraint);
					}
				}
				if (nextType.getName().equals(constraint.getRight().getSecondDataTypeName())) {
					//it can also be the seconf type
					if (constraint.getRight().getDataTypeName() == null ||
							noIncomingEdges.contains(config.getProblemData().getTypeForName(constraint.getRight().getDataTypeName()))) {
						this.addTypesFromExpressionNoDuplicates(haveEdgeFromType, constraint.getLeft());
						toBeRemoved.add(constraint);
					}
				}
			}
			constraintCopyList.removeAll(toBeRemoved);
			for (IGenerateDataType edgeType : haveEdgeFromType) {
				boolean otherIncomingEdges = false;
				for (ProblemGenerationConstraint constraint : constraintCopyList) {
					if (edgeType.getName().equals(constraint.getLeft().getDataTypeName()) ||
							edgeType.getName().equals(constraint.getLeft().getSecondDataTypeName())) {
						if ((constraint.getRight().getDataTypeName() == null ||
								config.getProblemData().getTypeForName(constraint.getRight().getDataTypeName()) == null) &&
								(constraint.getRight().getSecondDataTypeName() == null ||
										config.getProblemData().getTypeForName(constraint.getRight().getSecondDataTypeName()) == null)) {
							//all good
						}else {
							otherIncomingEdges = true;
						}
					}
				}
				if (!otherIncomingEdges) {
					noIncomingEdges.add(edgeType);
				}
			}
		}
		if (!constraintCopyList.isEmpty()) {
			//Check if the remaining constraints are based on numbers only
			for (ProblemGenerationConstraint constraint : constraintCopyList) {
				if ((constraint.getRight().getDataTypeName() != null &&
						config.getProblemData().getTypeForName(constraint.getRight().getDataTypeName()) == null) ||
						(constraint.getRight().getSecondDataTypeName() != null &&
								config.getProblemData().getTypeForName(constraint.getRight().getSecondDataTypeName()) == null)) {
					throw new IllegalStateException("The constraints include at least one cycle!");
				}

			}
		}
		return order;
	}

	private List<IGenerateDataType> addTypesFromExpressionNoDuplicates(List<IGenerateDataType> typeList, IExpression expression) {
		IGenerateDataType firstType = config.getProblemData().getTypeForName(expression.getDataTypeName());
		IGenerateDataType secondType = config.getProblemData().getTypeForName(expression.getSecondDataTypeName());
		if (firstType != null && !typeList.contains(firstType)) {
			typeList.add(firstType);
		}
		if (secondType != null && !typeList.contains(secondType)) {
			typeList.add(secondType);
		}
		return typeList;
	}

}
