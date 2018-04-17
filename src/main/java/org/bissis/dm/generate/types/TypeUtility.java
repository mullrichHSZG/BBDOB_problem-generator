package org.bissis.dm.generate.types;

import org.bissis.dm.generate.data.ProblemGenerationParameter;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by bissi on 12.03.2018.
 */
public class TypeUtility {

    public static IGenerateDataType createSimpleDataTypeFor(JSONObject typeDescription) {
        String type = (String) typeDescription.get("type");
        IGenerateDataType returnType;
        switch (type) {
            case "integer": {
                int value = (Integer) typeDescription.get("value");
                //returnType = new SimpleNumberDataType(value, true);
                break;
            }
            case "double": {
                double value = (Double) typeDescription.get("value");
                //returnType = new SimpleNumberDataType(value, false);
                break;
            }
            default : {
                returnType = null; //TODO: other default type? and fix/delete this class
                break;
            }
        }
        return null;
    }

    public static IGenerateDataType createGenerateDataTypeFor(JSONObject typeDescription) { //TODO: add parameters
        String type = (String) typeDescription.get("type");
        IGenerateDataType returnType;
        switch (type) {
            case "integer": {
                //min, max or expression
                Integer value = (Integer) typeDescription.get("value");
                String expression = (String) typeDescription.get("expression");
                Integer defaultValue = (Integer) typeDescription.get("default");
                if(expression == null) {
                    if (value == null) {
                        int min = (Integer) typeDescription.get("min");
                        int max = (Integer) typeDescription.get("max");
                        if (defaultValue == null) {
                            defaultValue = (max - min) / 2;
                        }
                        //returnType = new NumericGenerateDataType(min, max, true, defaultValue);
                    } else {
                        //returnType = new SimpleNumberDataType(value, true);
                    }
                } else {

                }
                //returnType = new SimpleNumberDataType(value, true);
                break;
            }
            case "double": {
                //min, max or expression
                double value = (Double) typeDescription.get("value");
                //returnType = new SimpleNumberDataType(value, false);
                break;
            }
            default : {
                returnType = null; //TODO: other default type?
                break;
            }
        }
        return null;
    }

}
