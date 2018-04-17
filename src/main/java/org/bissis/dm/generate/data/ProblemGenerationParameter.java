package org.bissis.dm.generate.data;

import org.bissis.dm.generate.types.IGenerateDataType;
import org.bissis.dm.generate.types.SimpleBooleanDataType;
import org.bissis.dm.generate.types.SimpleNominalDataType;
import org.bissis.dm.generate.types.SimpleNumberDataType;
import org.json.simple.JSONObject;

/**
 * Created by bissi on 11.03.2018.
 */
public class ProblemGenerationParameter {

    private String name;
    private IGenerateDataType type; //The type will provide the value

    public ProblemGenerationParameter(JSONObject jsonParameter) {
        this.name = (String) jsonParameter.get("name");
        String typeName = (String) jsonParameter.get("type");
        switch (typeName) {
            case ("integer"): {
                long value = (Long) jsonParameter.get("value");
                this.type = new SimpleNumberDataType(this.name, value, true);
                break;
            } case ("double"): {
                double value = (Double) jsonParameter.get("value");
                this.type = new SimpleNumberDataType(this.name, value, false);
                break;
            } case ("boolean"): {
                boolean value = (Boolean) jsonParameter.get("value");
                this.type = new SimpleBooleanDataType(this.name ,value);
                break;
            } case ("nominal"): {
                String value = (String) jsonParameter.get("value");
                this.type = new SimpleNominalDataType(this.name, value);
                break;
            } default: {
                this.type = null;
            }
        }
    }


    public String getName() {
        return this.name;
    }

    public IGenerateDataType getDataType() {
        return this.type;
    }

}
