package org.bissis.dm.generate.types;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bissi on 02.04.2018.
 */
public class NaNGeneratorTest {

    private NaNGenerator gen1, gen2;

    @Before
    public void setUp() throws Exception {
        gen1 = new NaNGenerator(null);
        gen2 = new NaNGenerator("NaN");
    }

    @After
    public void tearDown() throws Exception {
        gen1 = null;
        gen2 = null;
    }

    @Test
    public void getTotalUpperBound() throws Exception {
        Assert.assertEquals(Double.NaN, gen1.getTotalUpperBound(), Double.NaN);
        Assert.assertEquals(Double.NaN, gen2.getTotalUpperBound(), Double.NaN);
    }

    @Test
    public void getTotalLowerBound() throws Exception {
        Assert.assertEquals(Double.NaN, gen1.getTotalLowerBound(), Double.NaN);
        Assert.assertEquals(Double.NaN, gen2.getTotalLowerBound(), Double.NaN);
    }

    @Test
    public void getNextUpperBound() throws Exception {
        Assert.assertEquals(Double.NaN, gen1.getNextUpperBound(), Double.NaN);
        Assert.assertEquals(Double.NaN, gen2.getNextUpperBound(), Double.NaN);
    }

    @Test
    public void getNextLowerBound() throws Exception {
        Assert.assertEquals(Double.NaN, gen1.getNextLowerBound(), Double.NaN);
        Assert.assertEquals(Double.NaN, gen2.getNextLowerBound(), Double.NaN);
    }

    @Test
    public void allowIntegersOnly() throws Exception {
        Assert.assertFalse(gen1.allowIntegersOnly());
        Assert.assertFalse(gen2.allowIntegersOnly());
    }

    @Test
    public void generateValue() throws Exception {
        Assert.assertEquals("NaN", gen1.generateValue());
        Assert.assertEquals("NaN", gen2.generateValue());
    }

    @Test
    public void getDefaultValue() throws Exception {
        Assert.assertEquals("NaN", gen1.getDefaultValue());
        Assert.assertEquals("NaN", gen2.getDefaultValue());
    }

    @Test
    public void getTotalDistinctValues() throws Exception {
        Assert.assertEquals(1, gen1.getTotalDistinctValues());
        Assert.assertEquals(1, gen2.getTotalDistinctValues());
    }

    @Test
    public void getNextDistinctValues() throws Exception {
        Assert.assertEquals(1, gen1.getNextDistinctValues());
        Assert.assertEquals(1, gen2.getNextDistinctValues());
    }

}