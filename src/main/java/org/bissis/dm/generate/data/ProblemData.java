package org.bissis.dm.generate.data;

import org.bissis.dm.generate.ProblemGenerationConfiguration;
import org.bissis.dm.generate.types.IGenerateDataType;

import java.util.*;

/**
 * Created by bissi on 13.03.2018.
 */
public class ProblemData {

    private HashMap<String, List<String>> data;
    private HashMap<String, IGenerateDataType> types;
    private ProblemGenerationConfiguration configuration;

    private int maxTries = 1000; //TODO: make this configurable

    public ProblemData() {
        this.types = new HashMap<>();
        this.data = new HashMap<>();
    }

    public void setConfiguration(ProblemGenerationConfiguration configuration) {
        this.configuration = configuration;
    }

    public ProblemGenerationConfiguration getConfiguration() {
        return this.configuration;
    }

    public void addDataType(IGenerateDataType dataType) {
        this.types.put(dataType.getName(), dataType);
        this.data.put(dataType.getName(), new ArrayList<String>());
    }

    public void generateData(List<ProblemGenerationConstraint> constraints, IGenerateDataType[] order) {
        int index = 0;
        IGenerateDataType currentType;
        boolean[] potentialDuplicateRows = new boolean[this.data.get(order[index].getName()).size()];
        for (int row = 0 ; row < potentialDuplicateRows.length ; row++) {
            potentialDuplicateRows[row] = true;
        }
        while(index < order.length) {
            currentType = order[index];
            int tries = this.maxTries;
            boolean constraintsFulfilled = false;
            String value = "";
            while(!constraintsFulfilled) {
                tries--;
                constraintsFulfilled = true;
                value = currentType.generateValue();
                for (ProblemGenerationConstraint constraint : constraints) {
                        if (constraint.getLeft().getDataTypeName().equals(currentType.getName())) {
                            //TODO: the part below until the next TODO belongs into the actual constraint
                            String secondLeft = constraint.getLeft().getSecondDataTypeName();
                            String left = constraint.getLeft().evaluate(value, this.getLastValueFor(secondLeft));
                            String compareWith = constraint.getRight().getDataTypeName();
                            String secondRight = constraint.getRight().getSecondDataTypeName();
                            String right = constraint.getRight().evaluate(this.getLastValueFor(compareWith), this.getLastValueFor(secondRight));
                            //TODO: needs to be done differently as well... Numbers or else...
                            constraintsFulfilled &= constraint.isStillPossible(configuration.getNumberOfRows() - (this.data.get(currentType.getName()).size()), left, right, this);
                        }
                }
                String typeName = order[index].getName();
                if (this.configuration.isNoDuplicates()) {
                    boolean[] potentialDuplicateCopy = Arrays.copyOf(potentialDuplicateRows, potentialDuplicateRows.length);
                    boolean atLeastOneDuplicate = false;
                    int potentialDuplicateRowCount = 0;
                    for (int row = 0; row < potentialDuplicateRows.length; row++) {
                        String valueForType = this.data.get(typeName).get(row);
                        potentialDuplicateRows[row] &= value.equals(valueForType);
                        atLeastOneDuplicate |= potentialDuplicateRows[row];
                        if (potentialDuplicateRows[row]) {
                            potentialDuplicateRowCount++;
                        }
                    }
                    if (atLeastOneDuplicate) {
                        boolean duplicateNotPreventable = true;
                        long potentialCombinations = 0;
                        for (int secondIndex = index + 1; secondIndex < order.length; secondIndex++) {
                            if (order[secondIndex].getTotalDistinctValues() > potentialDuplicateRowCount) {
                                duplicateNotPreventable = false;
                                break;
                            }
                            if (potentialCombinations == 0) {
                                potentialCombinations += order[secondIndex].getTotalDistinctValues();
                            } else {
                                potentialCombinations *= order[secondIndex].getTotalDistinctValues();
                            }
                        }
                        duplicateNotPreventable &= potentialCombinations <= potentialDuplicateRowCount;
                        constraintsFulfilled &= !duplicateNotPreventable;
                        if (duplicateNotPreventable) {
                            potentialDuplicateRows = Arrays.copyOf(potentialDuplicateCopy, potentialDuplicateCopy.length);
                        }
                    }
                }
                if (!constraintsFulfilled) {
                    if (tries == 0) {
                        throw new IllegalStateException("No possible value for the current type (" + typeName + ") could be generated after " + maxTries + " tries.");
                        //constraintsFulfilled = true;
                        //value = currentType.getDefaultValue();
                    } else {
                        currentType.resetLastValue(); //TODO: complete reset needs to be implemented in case of a loop and in case of all distinct is selected!
                    }
                }
            }
            this.data.get(currentType.getName()).add(value);
            index++;
        }
    }

    public Set<String> getDataTypes(){
        return this.data.keySet();
    }

    public List<String> getValuesForName(String name) {
        return this.data.get(name);
    }

    public String getLastValueFor(String dataTypeName) {
        if (this.data.containsKey(dataTypeName)) {
            if (this.data.get(dataTypeName).size() == 0)
                return null;
            return this.data.get(dataTypeName).get(data.get(dataTypeName).size() - 1);
        }
        return null;
    }

    public IGenerateDataType getTypeForName(String name) {
        return this.types.get(name);
    }

    //TODO: ProblemData will take care of generating new values altogether

}
