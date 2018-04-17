package org.bissis.dm.generate.expressions;

import org.bissis.dm.generate.ProblemGenerationConfiguration;
import org.bissis.dm.generate.data.ProblemData;
import org.bissis.dm.generate.types.IGenerateDataType;

/**
 * Created by bissi on 11.03.2018.
 */
public interface IExpression {

    public String evaluate(String value, String secondValue);

    public String getExpression();

    public String getDataTypeName();

    /**
     * The name of the second data type if present. Otherwise this method should return null.
     * @return
     */
    public String getSecondDataTypeName();

    public double getMinValue(ProblemData problemData);

    public double getMaxValue(ProblemData problemData);

}
