package org.bissis.dm.generate.types;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by bissi on 17.04.2018.
 */
public class RandomStepDataTypeTest {

    private RandomStepDataType randomStepMinMax, randomStepMinStopRows, randomStepMinStopRowsExtreme;

    @Before
    public void setUp(){
        this.randomStepMinMax = new RandomStepDataType("MinMax", -12, 4, 20, new Random());
        this.randomStepMinStopRows = new RandomStepDataType("StopRows", 100, 10, new Random(), 1000, 50);
        //The extreme case does not allow for a different increment than 10, the generator should be able to allow these cases as well and fulfill them
        this.randomStepMinStopRowsExtreme = new RandomStepDataType("StopRowsExtreme", 10, 10, new Random(), 1000, 100);
    }

    @After
    public void tearDown(){
        this.randomStepMinMax = null;
        this.randomStepMinStopRows = null;
        this.randomStepMinStopRowsExtreme = null;
    }

    @Test
    public void generateValue() throws Exception {
        String lastGenMinMax = this.randomStepMinMax.generateValue();
        String lastGenMinStopRows = this.randomStepMinStopRows.generateValue();
        String lastGenMinStopRowsExtreme = this.randomStepMinStopRowsExtreme.generateValue();
        Assert.assertEquals("-12", lastGenMinMax);
        Assert.assertEquals("100", lastGenMinStopRows);
        Assert.assertEquals("10", lastGenMinStopRowsExtreme);
        int rows = 49;
        for (int i = 0 ; i < rows ; i++) {
            String nextGenMinMax = this.randomStepMinMax.generateValue();
            String nextGenMinStopRows = this.randomStepMinStopRows.generateValue();
            String nextGenMinStopRowsExtreme = this.randomStepMinStopRowsExtreme.generateValue();
            Assert.assertTrue(Double.parseDouble(nextGenMinMax) >= Double.parseDouble(lastGenMinMax) + 4);
            Assert.assertTrue(Double.parseDouble(nextGenMinMax) <= Double.parseDouble(lastGenMinMax) + 20);
            lastGenMinMax = nextGenMinMax;
            Assert.assertTrue(Double.parseDouble(nextGenMinStopRows) >= Double.parseDouble(lastGenMinStopRows) + 10);
            Assert.assertTrue(Double.parseDouble(nextGenMinStopRows) <= 1000);
            lastGenMinStopRows = nextGenMinStopRows;
            Assert.assertEquals(Long.parseLong(lastGenMinStopRowsExtreme) + 10, Long.parseLong(nextGenMinStopRowsExtreme));
            lastGenMinStopRowsExtreme = nextGenMinStopRowsExtreme;
        }
        Assert.assertEquals("1000", lastGenMinStopRows);
        rows = 50;
        for (int i = 0 ; i < rows ; i++) {
            String nextGenMinMax = this.randomStepMinMax.generateValue();
            String nextGenMinStopRows = this.randomStepMinStopRows.generateValue();
            String nextGenMinStopRowsExtreme = this.randomStepMinStopRowsExtreme.generateValue();
            Assert.assertTrue(Double.parseDouble(nextGenMinMax) >= Double.parseDouble(lastGenMinMax) + 4);
            Assert.assertTrue(Double.parseDouble(nextGenMinMax) <= Double.parseDouble(lastGenMinMax) + 20);
            lastGenMinMax = nextGenMinMax;
            Assert.assertEquals("1000", lastGenMinStopRows);
            lastGenMinStopRows = nextGenMinStopRows;
            Assert.assertEquals(Long.parseLong(lastGenMinStopRowsExtreme) + 10, Long.parseLong(nextGenMinStopRowsExtreme));
            lastGenMinStopRowsExtreme = nextGenMinStopRowsExtreme;
        }
        try {
            this.randomStepMinStopRowsExtreme = new RandomStepDataType("StopRowsExtreme11", 11, 10, new Random(), 1000, 100);
            Assert.fail("Creating a generator with these settings should not be allowed as it is impossible to fulfill these constraints.");
        }catch(IllegalArgumentException ex) {
            //OK
        }catch(Exception e) {
            Assert.fail("The wrong exception has been thrown. Expected an IllegalArgumentException instead of a " + e.getClass().getName() + ".");
        }
    }

    @Test
    public void getDefaultValue() throws Exception {
        //the expected default value for this type is the current start value (which is the next value that will be generated)
        Assert.assertEquals("-12", this.randomStepMinMax.getDefaultValue());
        Assert.assertEquals("100", this.randomStepMinStopRows.getDefaultValue());
        Assert.assertEquals("10", this.randomStepMinStopRowsExtreme.getDefaultValue());
        //This method call should not have any side effects
        Assert.assertEquals("-12", this.randomStepMinMax.getDefaultValue());
        Assert.assertEquals("100", this.randomStepMinStopRows.getDefaultValue());
        Assert.assertEquals("10", this.randomStepMinStopRowsExtreme.getDefaultValue());

        this.randomStepMinMax.generateValue();
        this.randomStepMinStopRows.generateValue();
        this.randomStepMinStopRowsExtreme.generateValue();
        //The next value that is generated should be the same as the default
        String defaultMinMax = this.randomStepMinMax.getDefaultValue();
        String defaultMinStopRows = this.randomStepMinStopRows.getDefaultValue();
        String defaultMinStopRowsExtreme = this.randomStepMinStopRowsExtreme.getDefaultValue();

        Assert.assertEquals(this.randomStepMinMax.generateValue(), defaultMinMax);
        Assert.assertEquals(this.randomStepMinStopRows.generateValue(), defaultMinStopRows);
        Assert.assertEquals(this.randomStepMinStopRowsExtreme.generateValue(), defaultMinStopRowsExtreme);
        //Assert.assertTrue(Double.parseDouble(this.randomStepMinMax.getDefaultValue()) >= 8.0);
        //Assert.assertTrue(Double.parseDouble(this.randomStepMinStopRows.getDefaultValue()) >= 110.0);
        //Assert.assertEquals("110", this.randomStepMinStopRowsExtreme.getDefaultValue());
    }

    @Test
    public void resetLastValue() throws Exception {
        String genMinMax = this.randomStepMinMax.generateValue();
        String genMinStopRows = this.randomStepMinStopRows.generateValue();
        String genMinStopRowsExtreme = this.randomStepMinStopRowsExtreme.generateValue();
        int rows = 50;
        for (int i = 0 ; i < rows ; i++) {
            this.randomStepMinMax.resetLastValue();
            this.randomStepMinStopRows.resetLastValue();
            this.randomStepMinStopRowsExtreme.resetLastValue();
            Assert.assertEquals(genMinMax, this.randomStepMinMax.generateValue());
            Assert.assertEquals(genMinStopRows, this.randomStepMinStopRows.generateValue());
            Assert.assertEquals(genMinStopRowsExtreme, this.randomStepMinStopRowsExtreme.generateValue());
            genMinMax = this.randomStepMinMax.generateValue();
            genMinStopRows = this.randomStepMinStopRows.generateValue();
            genMinStopRowsExtreme = this.randomStepMinStopRowsExtreme.generateValue();
        }
    }

    @Test
    public void getTotalDistinctValues() throws Exception {
        Assert.assertEquals(Long.MAX_VALUE / 4 + 3, this.randomStepMinMax.getTotalDistinctValues());
        Assert.assertEquals(50, this.randomStepMinStopRows.getTotalDistinctValues());
        Assert.assertEquals(100, this.randomStepMinStopRowsExtreme.getTotalDistinctValues());
    }

    @Test
    public void getNextDistinctValues() throws Exception {
        Assert.assertEquals(16, this.randomStepMinMax.getNextDistinctValues());
        Assert.assertEquals(1, this.randomStepMinStopRows.getNextDistinctValues()); //The random step generator has no reset functionality as of yet
        Assert.assertEquals(1, this.randomStepMinStopRowsExtreme.getNextDistinctValues());
        int rows = 10;
        for (int i = 0 ; i < rows ; i++) {
            this.randomStepMinMax.generateValue();
            this.randomStepMinStopRows.generateValue();
            this.randomStepMinStopRowsExtreme.generateValue();
            Assert.assertEquals(16, this.randomStepMinMax.getNextDistinctValues());
            Assert.assertEquals(1, this.randomStepMinStopRows.getNextDistinctValues()); //The random step generator has no reset functionality as of yet
            Assert.assertEquals(1, this.randomStepMinStopRowsExtreme.getNextDistinctValues());
        }
    }

    @Test
    public void getTotalUpperBound() throws Exception {
        Assert.assertEquals(Long.MAX_VALUE, this.randomStepMinMax.getTotalUpperBound(), 0);
        Assert.assertEquals(1000, this.randomStepMinStopRows.getTotalUpperBound(), 0); //The random step generator has no reset functionality as of yet
        Assert.assertEquals(1000, this.randomStepMinStopRowsExtreme.getTotalUpperBound(), 0);
    }

    @Test
    public void getTotalLowerBound() throws Exception {
        Assert.assertEquals(-12, this.randomStepMinMax.getTotalLowerBound(), 0);
        Assert.assertEquals(100, this.randomStepMinStopRows.getTotalLowerBound(), 0); //The random step generator has no reset functionality as of yet
        Assert.assertEquals(10, this.randomStepMinStopRowsExtreme.getTotalLowerBound(), 0);
    }

    @Test
    public void getNextUpperBound() throws Exception {
        Assert.assertEquals(8, this.randomStepMinMax.getNextUpperBound(), 0);
        //the upper and lower bound for the next value should be the same if a pre--generated list is used
        Assert.assertEquals(this.randomStepMinStopRows.getNextLowerBound(), this.randomStepMinStopRows.getNextUpperBound(), 0);
        Assert.assertEquals(this.randomStepMinStopRowsExtreme.getNextLowerBound(), this.randomStepMinStopRowsExtreme.getNextUpperBound(), 0);
        int rows = 10;
        for (int i = 0 ; i < rows ; i++) {
            this.randomStepMinMax.generateValue();
            String genMinMax = this.randomStepMinMax.getDefaultValue();
            this.randomStepMinStopRows.generateValue();
            this.randomStepMinStopRowsExtreme.generateValue();
            Assert.assertEquals(Double.parseDouble(genMinMax) + 20, this.randomStepMinMax.getNextUpperBound(), 0);
            Assert.assertEquals(this.randomStepMinStopRows.getNextLowerBound(), this.randomStepMinStopRows.getNextUpperBound(), 0);
            Assert.assertEquals(this.randomStepMinStopRowsExtreme.getNextLowerBound(), this.randomStepMinStopRowsExtreme.getNextUpperBound(), 0);
        }
    }

    @Test
    public void getNextLowerBound() throws Exception {
        Assert.assertEquals(-8, this.randomStepMinMax.getNextLowerBound(), 0);
        //The tests for equality for the other generators can be omitted here
        int rows = 10;
        for (int i = 0 ; i < rows ; i++) {
            this.randomStepMinMax.generateValue();
            String genMinMax = this.randomStepMinMax.getDefaultValue();
            Assert.assertEquals(Double.parseDouble(genMinMax) + 4, this.randomStepMinMax.getNextLowerBound(), 0);
        }
    }

    @Test
    public void allowIntegersOnly() throws Exception {
        Assert.assertTrue(this.randomStepMinMax.allowIntegersOnly());
        Assert.assertTrue(this.randomStepMinStopRows.allowIntegersOnly());
        Assert.assertTrue(this.randomStepMinStopRowsExtreme.allowIntegersOnly());
    }

}