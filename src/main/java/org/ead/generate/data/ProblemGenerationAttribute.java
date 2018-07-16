package org.ead.generate.data;

import org.ead.generate.types.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Random;

/**
 * Class for managing a single attribute.
 * Basically, it stores the generator (or data type) and several other configuration values.
 * @author Markus Ullrich
 */
@SuppressWarnings("FieldCanBeLocal")
public class ProblemGenerationAttribute {

    private String name;
    private IGenerateDataType type;
    private boolean useAllValues = false;
    private boolean output;
    private boolean useLocalSeed;
    private Long localSeed;

    private double outputProbability;
    private boolean outputProbabilitySet;

    @SuppressWarnings("ConstantConditions")
    public ProblemGenerationAttribute(JSONObject jsonAttribute, ProblemData problemData, Random globalRandom, long rowCount) {
        this.name = (String) jsonAttribute.get("name");
        Boolean useAllValues = (Boolean) jsonAttribute.get("use_all_values");
        if(useAllValues != null) {
            this.useAllValues = useAllValues;
        }
        Boolean output = (Boolean) jsonAttribute.get("output");
        this.output = output == null ? true : output;
        Double outputProbability = (Double) jsonAttribute.get("output_probability");
        if (outputProbability != null) {
            this.outputProbability = outputProbability;
            this.outputProbabilitySet = true;
        } else {
            this.outputProbabilitySet = false;
        }
        Long localSeed = (Long) jsonAttribute.get("seed");
        if (localSeed != null) {
            this.localSeed = localSeed;
            this.useLocalSeed = true;
        } else {
            this.localSeed = null;
            this.useLocalSeed = false;
        }
        String typeName = (String) jsonAttribute.get("type");
        switch (typeName) {
            case ("integer"): {
                String expression = (String) jsonAttribute.get("expression");
                if (expression == null) {
                    Long value = (Long) jsonAttribute.get("value");
                    if (value == null) {
                        long min = (Long) jsonAttribute.get("min");
                        long max = (Long) jsonAttribute.get("max");
                        Long defaultValue = (Long) jsonAttribute.get("default");
                        this.type = new NumericGenerateDataType(this.name, min, max, true, defaultValue == null ? Double.NaN : defaultValue, useLocalSeed ? new Random(localSeed) : globalRandom, this.useAllValues, rowCount);
                    } else {
                        this.type = new SimpleNumberDataType(this.name, value, true);
                    }
                } else {
                    this.type = new ExpressionDataType(expression, this.name, problemData);
                }
                break;
            }
            case ("double"): {
                String expression = (String) jsonAttribute.get("expression");
                if (expression == null) {
                    Double value = (Double) jsonAttribute.get("value");
                    if (value == null) {
                        double min = (Double) jsonAttribute.get("min");
                        double max = (Double) jsonAttribute.get("max");
                        Double defaultValue = (Double) jsonAttribute.get("default");
                        this.type = new NumericGenerateDataType(this.name, min, max, false, defaultValue == null ? Double.NaN : defaultValue, useLocalSeed ? new Random(localSeed) : globalRandom, this.useAllValues, rowCount);
                    } else {
                        this.type = new SimpleNumberDataType(this.name, value, false);
                    }
                } else {
                    this.type = new ExpressionDataType(expression, this.name, problemData);
                }
                break;
            }
            case ("id"): {
                long start = (Long) jsonAttribute.get("start");
                Long increment = (Long) jsonAttribute.get("increment");
                if(increment != null)
                    this.type = new IDGenerateDataType(this.name, start, increment);
                else {
                    long minIncrement = (Long) jsonAttribute.get("minIncrement");
                    Long maxIncrement = (Long) jsonAttribute.get("maxIncrement");
                    if (maxIncrement == null) {
                        Long stop = (Long) jsonAttribute.get("stop");
                        if (stop == null) {
                            this.type = new IDGenerateDataType(this.name, start);
                        } else {
                            this.type = new RandomStepDataType(this.name, start, minIncrement, useLocalSeed ? new Random(localSeed) : globalRandom, stop, rowCount);
                        }
                    } else {
                        this.type = new RandomStepDataType(this.name, start, minIncrement, maxIncrement, useLocalSeed ? new Random(localSeed) : globalRandom);
                    }
                }
                break;
            }
            case ("Gaussian") : {
                double mean = (Double) jsonAttribute.get("mean");
                double standard_deviation = (Double) jsonAttribute.get("standard_deviation");
                this.type = new GaussianDistributionDataType(this.name, mean, standard_deviation, useLocalSeed ? new Random(localSeed) : globalRandom, false);
                break;
            } case ("nominal") : {
                JSONArray values = (JSONArray) jsonAttribute.get("values");
                this.type = new NominalGenerateDataType(this.name);
                Iterator iter = values.iterator();
                //noinspection WhileLoopReplaceableByForEach
                while(iter.hasNext()) {
                    JSONObject value = (JSONObject) iter.next();
                    Boolean defaultValue = (Boolean) value.get("default");
                    ((NominalGenerateDataType) this.type).addOption( (String) value.get("value"), defaultValue == null ? false : defaultValue);
                }
                break;
            } case ("boolean") : {
                Double truePercentage = (Double) jsonAttribute.get("true");
                if (truePercentage == null) {
                    this.type = new BooleanDataType(this.name, useLocalSeed ? new Random(localSeed) : globalRandom);
                } else {
                    this.type = new BooleanDataType(this.name, truePercentage, useLocalSeed ? new Random(localSeed) : globalRandom);
                }
                break;
            } default: {
                this.type = new NaNGenerator("");
                break;
            }
        }
    }

    public IGenerateDataType getType() {
        return this.type;
    }

    public boolean isOutput(){
        return output;
    }

    public Long getLocalSeed(){
        return this.localSeed;
    }

    public double getOutputProbability() {
        return outputProbability;
    }

    public boolean isOutputProbabilitySet() {
        return outputProbabilitySet;
    }

    public boolean isUseAllValues() {
        return useAllValues;
    }
}

