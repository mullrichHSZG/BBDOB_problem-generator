package org.ead.generate.types;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bissi on 14.04.2018.
 */
public class IDGenerateDataTypeTest {

    private IDGenerateDataType idSimple1, id0Step10;

    @Before
    public void setUp() throws Exception {
        idSimple1 = new IDGenerateDataType("Simple ID", 1);
        id0Step10 = new IDGenerateDataType("10 Step Type", 0, 10);
    }

    @After
    public void tearDown() throws Exception {
        idSimple1 = null;
        id0Step10 = null;
    }

    @Test
    public void generateValue() throws Exception {
        double expectedSimple1Value = 1.0;
        double expected0Step10Value = 0.0;
        for (int i = 0 ; i < 1000 ; i++) {
            Assert.assertEquals(expectedSimple1Value, Double.parseDouble(idSimple1.generateValue()), 0.0);
            Assert.assertEquals(expected0Step10Value, Double.parseDouble(id0Step10.generateValue()), 0.0);
            expectedSimple1Value += 1;
            expected0Step10Value += 10;
        }
    }

    @Test
    public void getDefaultValue() throws Exception {
        Assert.assertEquals(1.0, Double.parseDouble(idSimple1.getDefaultValue()), 0.0);
        Assert.assertEquals(0.0, Double.parseDouble(id0Step10.getDefaultValue()), 0.0);
        idSimple1.generateValue(); //default = 2.0
        id0Step10.generateValue(); //default = 10.0
        Assert.assertEquals(2.0, Double.parseDouble(idSimple1.getDefaultValue()), 0.0);
        Assert.assertEquals(10.0, Double.parseDouble(id0Step10.getDefaultValue()), 0.0);
    }

    @Test
    public void resetLastValue() throws Exception {
        Assert.assertEquals(1.0, Double.parseDouble(idSimple1.getDefaultValue()), 0.0);
        Assert.assertEquals(0.0, Double.parseDouble(id0Step10.getDefaultValue()), 0.0);
        idSimple1.generateValue(); // 1.0
        id0Step10.generateValue(); // 0.0
        idSimple1.resetLastValue(); // next value is 1.0
        id0Step10.resetLastValue(); // next value is 0.0
        Assert.assertEquals(1.0, Double.parseDouble(idSimple1.generateValue()), 0.0);
        Assert.assertEquals(0.0, Double.parseDouble(id0Step10.generateValue()), 0.0);
        idSimple1.generateValue(); // 2.0
        id0Step10.generateValue(); // 10.0
        idSimple1.generateValue(); // 3.0
        id0Step10.generateValue(); // 20.0
        idSimple1.resetLastValue(); // next value is 3.0
        id0Step10.resetLastValue(); // next value is 20.0
        Assert.assertEquals(3.0, Double.parseDouble(idSimple1.generateValue()), 0.0);
        Assert.assertEquals(20.0, Double.parseDouble(id0Step10.generateValue()), 0.0);
    }

    @Test
    public void getDistinctValues() throws Exception {
        Assert.assertEquals(Long.MAX_VALUE - 1, idSimple1.getTotalDistinctValues());
        Assert.assertEquals(Long.MAX_VALUE / 10, id0Step10.getTotalDistinctValues());
    }

    @Test
    public void getTotalUpperBound() throws Exception {
        Assert.assertEquals(Long.MAX_VALUE, idSimple1.getTotalUpperBound(), 0.0);
        Assert.assertEquals(Long.MAX_VALUE - (Long.MAX_VALUE % 10), id0Step10.getTotalUpperBound(), 0.0);
        idSimple1.generateValue(); // total upper bound should not change
        id0Step10.generateValue(); // total upper bound should not change
        Assert.assertEquals(Long.MAX_VALUE, idSimple1.getTotalUpperBound(), 0.0);
        Assert.assertEquals(Long.MAX_VALUE - (Long.MAX_VALUE % 10), id0Step10.getTotalUpperBound(), 0.0);
    }

    @Test
    public void getTotalLowerBound() throws Exception {
        Assert.assertEquals(1, idSimple1.getTotalLowerBound(), 0.0);
        Assert.assertEquals(0, id0Step10.getTotalLowerBound(), 0.0);
        idSimple1.generateValue(); // total lower bound should not change
        id0Step10.generateValue(); // total lower bound should not change
        Assert.assertEquals(1, idSimple1.getTotalLowerBound(), 0.0);
        Assert.assertEquals(0, id0Step10.getTotalLowerBound(), 0.0);
    }

    @Test
    public void getNextLowerBound() throws Exception {
        Assert.assertEquals(2, idSimple1.getNextLowerBound(), 0.0);
        Assert.assertEquals(10, id0Step10.getNextLowerBound(), 0.0);
        idSimple1.generateValue(); //lower bound should change to 2.0 as well
        id0Step10.generateValue(); //lower bound should change to 10.0 as well
        Assert.assertEquals(3, idSimple1.getNextLowerBound(), 0.0);
        Assert.assertEquals(20, id0Step10.getNextLowerBound(), 0.0);
    }

    @Test
    public void getNextUpperBound() throws Exception {
        Assert.assertEquals(2, idSimple1.getNextUpperBound(), 0.0);
        Assert.assertEquals(10, id0Step10.getNextUpperBound(), 0.0);
        idSimple1.generateValue(); //upper bound should change to 2.0 as well
        id0Step10.generateValue(); //upper bound should change to 10.0 as well
        Assert.assertEquals(3, idSimple1.getNextUpperBound(), 0.0);
        Assert.assertEquals(20, id0Step10.getNextUpperBound(), 0.0);
    }

    @Test
    public void allowIntegersOnly() throws Exception {
        Assert.assertTrue(idSimple1.allowIntegersOnly());
        Assert.assertTrue(id0Step10.allowIntegersOnly());
    }

}