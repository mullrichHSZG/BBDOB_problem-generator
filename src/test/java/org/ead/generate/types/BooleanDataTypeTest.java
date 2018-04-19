package org.ead.generate.types;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by bissi on 02.04.2018.
 */
public class BooleanDataTypeTest {

    private BooleanDataType bool02, bool05, bool00, bool10, bool08;

    @Before
    public void setUp() throws Exception {
        bool02 = new BooleanDataType(null,0.2,new Random());
        bool05 = new BooleanDataType("Boolean", new Random());
        bool00 = new BooleanDataType(null,0.0,new Random());
        bool10 = new BooleanDataType(null,1.0,new Random());
        bool08 = new BooleanDataType(null,0.8,new Random());
    }

    @After
    public void tearDown() throws Exception {
        bool00 = null;
        bool02 = null;
        bool05 = null;
        bool08 = null;
        bool10 = null;
    }

    @Test
    public void setDefaultValue() throws Exception {
        bool05.setDefaultValue(true);
        Assert.assertEquals("true", bool05.getDefaultValue());
        bool05.setDefaultValue(false);
        Assert.assertEquals("false", bool05.getDefaultValue());
        bool02.setDefaultValue(false);
        Assert.assertEquals("false", bool02.getDefaultValue());
        try {
            bool10.setDefaultValue(false);
            Assert.fail("It should not be possible to set the default value to false if the true percentage is greater than 0.5");
        } catch(IllegalArgumentException ex) {
            //OK
        } catch(Exception e) {
            Assert.fail("Only IllegalArgumentException should have been thrown.");
        }
        try {
            bool08.setDefaultValue(false);
            Assert.fail("It should not be possible to set the default value to false if the true percentage is greater than 0.5");
        } catch(IllegalArgumentException ex) {
            //OK
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("Only IllegalArgumentException should have been thrown.");
        }
        try {
            bool02.setDefaultValue(true);
            Assert.fail("It should not be possible to set the default value to true if the true percentage is lower than 0.5");
        } catch(IllegalArgumentException ex) {
            //OK
        } catch(Exception e) {
            Assert.fail("Only IllegalArgumentException should have been thrown.");
        }
        try {
            bool00.setDefaultValue(true);
            Assert.fail("It should not be possible to set the default value to true if the true percentage is lower than 0.5");
        } catch(IllegalArgumentException ex) {
            //OK
        } catch(Exception e) {
            Assert.fail("Only IllegalArgumentException should have been thrown.");
        }
    }

    @Test
    public void generateValue() throws Exception {
        double bool02Total = 0.0;
        double bool05Total = 0.0;
        double bool08Total = 0.0;
        double generatedValues = 1000.0;
        for (int i = 0 ; i < generatedValues ; i++) {
            double bool00Value = Boolean.parseBoolean(bool00.generateValue()) ? 1.0 : 0.0;
            double bool02Value = Boolean.parseBoolean(bool02.generateValue()) ? 1.0 : 0.0;
            double bool05Value = Boolean.parseBoolean(bool05.generateValue()) ? 1.0 : 0.0;
            double bool08Value = Boolean.parseBoolean(bool08.generateValue()) ? 1.0 : 0.0;
            double bool10Value = Boolean.parseBoolean(bool10.generateValue()) ? 1.0 : 0.0;
            Assert.assertNotEquals("A generator with a truePercentage of 0.0 cannot generate 'true'.", 1.0, bool00Value, 0.0);
            Assert.assertNotEquals("A generator with a truePercentage of 1.0 cannot generate 'false'.", 0.0, bool10Value, 0.0);
            bool02Total += bool02Value;
            bool05Total += bool05Value;
            bool08Total += bool08Value;
        }
        double expectedValue = generatedValues * 0.2;
        Assert.assertEquals("The expected number of generated 'true' values out of " + generatedValues + " for a generator" +
                " with a truePercentage of 0.2 should be roughly " + expectedValue + ".", expectedValue, bool02Total, 25.0);
        expectedValue = generatedValues * 0.5;
        Assert.assertEquals("The expected number of generated 'true' values out of " + generatedValues + " for a generator" +
                " with a truePercentage of 0.5 should be roughly " + expectedValue + ".", expectedValue, bool05Total, 25.0);
        expectedValue = generatedValues * 0.8;
        Assert.assertEquals("The expected number of generated 'true' values out of " + generatedValues + " for a generator" +
                " with a truePercentage of 0.8 should be roughly " + expectedValue + ".", expectedValue, bool08Total, 25.0);
    }

    @Test
    public void getDefaultValue() throws Exception {
        Assert.assertEquals("false", bool02.getDefaultValue());
        Assert.assertEquals("false", bool05.getDefaultValue());
        Assert.assertEquals("false", bool00.getDefaultValue());
        Assert.assertEquals("true", bool10.getDefaultValue());
        Assert.assertEquals("true", bool08.getDefaultValue());
    }

    @Test
    public void getTotalDistinctValues() throws Exception {
        Assert.assertEquals(2, bool02.getTotalDistinctValues());
        Assert.assertEquals(2, bool05.getTotalDistinctValues());
        Assert.assertEquals(1, bool00.getTotalDistinctValues());
        Assert.assertEquals(1, bool10.getTotalDistinctValues());
        Assert.assertEquals(2, bool08.getTotalDistinctValues());
    }

    @Test
    public void getNextDistinctValues() throws Exception {
        Assert.assertEquals(2, bool02.getNextDistinctValues());
        Assert.assertEquals(2, bool05.getNextDistinctValues());
        Assert.assertEquals(1, bool00.getNextDistinctValues());
        Assert.assertEquals(1, bool10.getNextDistinctValues());
        Assert.assertEquals(2, bool08.getNextDistinctValues());
    }

    @Test
    public void resetLastValue() throws Exception {
        String value1 = bool02.generateValue();
        String value2 = bool05.generateValue();
        String value3 = bool00.generateValue();
        String value4 = bool10.generateValue();
        String value5 = bool08.generateValue();
        int loop = 1000;
        while(loop > 0) {
            loop--;
            bool02.resetLastValue();
            bool05.resetLastValue();
            bool00.resetLastValue();
            bool10.resetLastValue();
            bool08.resetLastValue();
            Assert.assertEquals(value1, bool02.generateValue());
            Assert.assertEquals(value2, bool05.generateValue());
            Assert.assertEquals(value3, bool00.generateValue());
            Assert.assertEquals(value4, bool10.generateValue());
            Assert.assertEquals(value5, bool08.generateValue());
            value1 = bool02.generateValue();
            value2 = bool05.generateValue();
            value3 = bool00.generateValue();
            value4 = bool10.generateValue();
            value5 = bool08.generateValue();
        }
    }

    @Test
    public void generateNumericValue() throws Exception {
        double bool02Total = 0.0;
        double bool05Total = 0.0;
        double bool08Total = 0.0;
        double generatedValues = 1000.0;
        for (int i = 0 ; i < generatedValues ; i++) {
            double bool00Value = bool00.generateNumericValue();
            double bool02Value = bool02.generateNumericValue();
            double bool05Value = bool05.generateNumericValue();
            double bool08Value = bool08.generateNumericValue();
            double bool10Value = bool10.generateNumericValue();
            Assert.assertNotEquals("A generator with a truePercentage of 0.0 cannot generate 'true'.", 1.0, bool00Value, 0.0);
            Assert.assertNotEquals("A generator with a truePercentage of 1.0 cannot generate 'false'.", 0.0, bool10Value, 0.0);
            bool02Total += bool02Value;
            bool05Total += bool05Value;
            bool08Total += bool08Value;
        }
        double expectedValue = generatedValues * 0.2;
        Assert.assertEquals("The expected number of generated 'true' values out of " + generatedValues + " for a generator" +
                " with a truePercentage of 0.2 should be roughly " + expectedValue + ".", expectedValue, bool02Total, 25.0);
        expectedValue = generatedValues * 0.5;
        Assert.assertEquals("The expected number of generated 'true' values out of " + generatedValues + " for a generator" +
                " with a truePercentage of 0.5 should be roughly " + expectedValue + ".", expectedValue, bool05Total, 25.0);
        expectedValue = generatedValues * 0.8;
        Assert.assertEquals("The expected number of generated 'true' values out of " + generatedValues + " for a generator" +
                " with a truePercentage of 0.8 should be roughly " + expectedValue + ".", expectedValue, bool08Total, 25.0);
    }

    @Test
    public void defaultNumericValue() throws Exception {
        Assert.assertEquals(0, bool02.defaultNumericValue(), 0.0);
        Assert.assertEquals(0, bool05.defaultNumericValue(), 0.0);
        Assert.assertEquals(0, bool00.defaultNumericValue(), 0.0);
        Assert.assertEquals(1, bool10.defaultNumericValue(), 0.0);
        Assert.assertEquals(1, bool08.defaultNumericValue(), 0.0);
    }

    @Test
    public void getTotalUpperBound() throws Exception {
        Assert.assertEquals(1, bool02.getTotalUpperBound(), 0.0);
        Assert.assertEquals(1, bool05.getTotalUpperBound(), 0.0);
        Assert.assertEquals(0, bool00.getTotalUpperBound(), 0.0);
        Assert.assertEquals(1, bool10.getTotalUpperBound(), 0.0);
        Assert.assertEquals(1, bool08.getTotalUpperBound(), 0.0);
    }

    @Test
    public void getTotalLowerBound() throws Exception {
        Assert.assertEquals(0, bool02.getTotalLowerBound(), 0.0);
        Assert.assertEquals(0, bool05.getTotalLowerBound(), 0.0);
        Assert.assertEquals(0, bool00.getTotalLowerBound(), 0.0);
        Assert.assertEquals(1, bool10.getTotalLowerBound(), 0.0);
        Assert.assertEquals(0, bool08.getTotalLowerBound(), 0.0);
    }

    @Test
    public void getNextUpperBound() throws Exception {
        Assert.assertEquals(1, bool02.getNextUpperBound(), 0.0);
        Assert.assertEquals(1, bool05.getNextUpperBound(), 0.0);
        Assert.assertEquals(0, bool00.getNextUpperBound(), 0.0);
        Assert.assertEquals(1, bool10.getNextUpperBound(), 0.0);
        Assert.assertEquals(1, bool08.getNextUpperBound(), 0.0);
    }

    @Test
    public void getNextLowerBound() throws Exception {
        Assert.assertEquals(0, bool02.getNextLowerBound(), 0.0);
        Assert.assertEquals(0, bool05.getNextLowerBound(), 0.0);
        Assert.assertEquals(0, bool00.getNextLowerBound(), 0.0);
        Assert.assertEquals(1, bool10.getNextLowerBound(), 0.0);
        Assert.assertEquals(0, bool08.getNextLowerBound(), 0.0);
    }

    @Test
    public void allowIntegersOnly() throws Exception {
        Assert.assertTrue(bool02.allowIntegersOnly());
        Assert.assertTrue(bool05.allowIntegersOnly());
        Assert.assertTrue(bool00.allowIntegersOnly());
        Assert.assertTrue(bool10.allowIntegersOnly());
        Assert.assertTrue(bool08.allowIntegersOnly());
    }

}