package org.bissis.dm.generate.types;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bissi on 14.04.2018.
 */
public class NominalGenerateDataTypeTest {

    private NominalGenerateDataType gen0Options, gen10Options, gen1Option, gen2Options;
    private final String option1 = "o1",
                         option2 = "o2",
                         option3 = "o3",
                         option4 = "o4",
                         option5 = "o5",
                         option6 = "o6",
                         option7 = "o7",
                         option8 = "o8",
                         option9 = "o9",
                         option10 = "o10",
                         duplicateOption = "o2";
    private int generationCounter;

    @Before
    public void setUp() throws Exception {
        gen0Options = new NominalGenerateDataType("0 options");
        gen1Option = new NominalGenerateDataType("1 option");
        gen1Option.addOption(option1, true);
        gen2Options = new NominalGenerateDataType("2 options");
        gen2Options.addOption(option1, false);
        gen2Options.addOption(option2, false);
        gen10Options = new NominalGenerateDataType("10 options");
        gen10Options.addOption(option1, false);
        gen10Options.addOption(option2, false);
        gen10Options.addOption(option3, false);
        gen10Options.addOption(option4, false);
        gen10Options.addOption(option5, true);
        gen10Options.addOption(option6, false);
        gen10Options.addOption(option7, false);
        gen10Options.addOption(option8, false);
        gen10Options.addOption(option9, false);
        gen10Options.addOption(option10, false);
        generationCounter = 10000;
    }

    @After
    public void tearDown() throws Exception {
        gen0Options = null;
        gen1Option = null;
        gen2Options = null;
        gen10Options = null;
    }

    @Test
    public void removeOption() throws Exception {
        // removing a non-existent option should not throw an exception, the method should do nothing instead
        gen0Options.removeOption(option2);
        gen1Option.removeOption(option6);
        gen2Options.removeOption(option10);
        gen10Options.removeOption("");
        testInitialGeneration();

        gen1Option.removeOption(option1);
        try {
            gen1Option.generateValue();
        } catch(IllegalStateException ex) {
            //good
        } catch(Exception e) {
            Assert.fail(e.getClass().getName() + " should not have been thrown, but an IllegalStateException instead.");
        }
        gen2Options.removeOption(option1);
        int option2Counter = 0;
        for (int i = 0; i < generationCounter; i++) {
            if (gen2Options.generateValue().equals(option2)) {
                option2Counter++;
            }
        }
        Assert.assertEquals(generationCounter, option2Counter);
        gen10Options.removeOption(option1);
        gen10Options.addOption(duplicateOption, false);
        gen10Options.removeOption(option3);
        gen10Options.removeOption(option5);
        gen10Options.removeOption(option7);
        gen10Options.removeOption(option9);
        Assert.assertNull(gen10Options.getDefaultValue());
        option2Counter = 0;
        int option4Counter = 0;
        int option6Counter = 0;
        int option8Counter = 0;
        int option10Counter = 0;
        for (int i = 0; i < generationCounter; i++) {
            String generatedValue = gen10Options.generateValue();
            if (generatedValue.equals(option2)) {
                option2Counter++;
            }
            if (generatedValue.equals(option4)) {
                option4Counter++;
            }
            if (generatedValue.equals(option6)) {
                option6Counter++;
            }
            if (generatedValue.equals(option8)) {
                option8Counter++;
            }
            if (generatedValue.equals(option10)) {
                option10Counter++;
            }
        }
        Assert.assertEquals(generationCounter, option2Counter + option4Counter + option6Counter + option8Counter + option10Counter);
        double delta = 300.0;
        Assert.assertEquals(0, option2Counter - 2 * option4Counter, delta); // make sure the values are roughly equal distributed option 2 should have been generated twice as much since it is a duplicate
        Assert.assertEquals(0, option2Counter - 2 * option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - 2 * option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - 2 * option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option6Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option6Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option8Counter - option10Counter, delta); // make sure the values are roughly equal distributed
    }

    @Test
    public void generateValue() throws Exception {
        try {
            gen0Options.generateValue();
        } catch(IllegalStateException ex) {
            //good
        } catch(Exception e) {
            Assert.fail(e.getClass().getName() + " should not have been thrown, but an IllegalStateException instead.");
        }
        testInitialGeneration();
    }

    private void testInitialGeneration() {
        int option1Counter = 0;
        for (int i = 0; i < generationCounter; i++) {
            if (gen1Option.generateValue().equals(option1)) {
                option1Counter++;
            }
        }
        Assert.assertEquals(generationCounter, option1Counter);
        option1Counter = 0;
        int option2Counter = 0;
        for (int i = 0; i < generationCounter; i++) {
            String generatedValue = gen2Options.generateValue();
            if (generatedValue.equals(option1)) {
                option1Counter++;
            }
            if (generatedValue.equals(option2)) {
                option2Counter++;
            }
        }
        Assert.assertEquals(generationCounter, option1Counter + option2Counter);
        Assert.assertEquals(0, option1Counter - option2Counter, 1000.0); // make sure the values are roughly equal distributed
        option1Counter = 0;
        option2Counter = 0;
        int option3Counter = 0;
        int option4Counter = 0;
        int option5Counter = 0;
        int option6Counter = 0;
        int option7Counter = 0;
        int option8Counter = 0;
        int option9Counter = 0;
        int option10Counter = 0;
        for (int i = 0; i < generationCounter; i++) {
            String generatedValue = gen10Options.generateValue();
            if (generatedValue.equals(option1)) {
                option1Counter++;
            }
            if (generatedValue.equals(option2)) {
                option2Counter++;
            }
            if (generatedValue.equals(option3)) {
                option3Counter++;
            }
            if (generatedValue.equals(option4)) {
                option4Counter++;
            }
            if (generatedValue.equals(option5)) {
                option5Counter++;
            }
            if (generatedValue.equals(option6)) {
                option6Counter++;
            }
            if (generatedValue.equals(option7)) {
                option7Counter++;
            }
            if (generatedValue.equals(option8)) {
                option8Counter++;
            }
            if (generatedValue.equals(option9)) {
                option9Counter++;
            }
            if (generatedValue.equals(option10)) {
                option10Counter++;
            }
        }
        Assert.assertEquals(generationCounter, option1Counter + option2Counter + option3Counter + option4Counter + option5Counter + option6Counter + option7Counter + option8Counter + option9Counter + option10Counter);
        double delta = 300.0;
        Assert.assertEquals(0, option1Counter - option2Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option3Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option4Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option5Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option7Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option1Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option3Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option4Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option5Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option7Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option2Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option4Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option5Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option7Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option3Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option5Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option7Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option4Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option5Counter - option6Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option5Counter - option7Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option5Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option5Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option5Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option6Counter - option7Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option6Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option6Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option6Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option7Counter - option8Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option7Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option7Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option8Counter - option9Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option8Counter - option10Counter, delta); // make sure the values are roughly equal distributed
        Assert.assertEquals(0, option9Counter - option10Counter, delta); // make sure the values are roughly equal distributed
    }

    @Test
    public void getDefaultValue() throws Exception {
        Assert.assertNull(gen0Options.getDefaultValue());
        Assert.assertNull(gen2Options.getDefaultValue());
        Assert.assertEquals(option1, gen1Option.getDefaultValue());
        Assert.assertEquals(option5, gen10Options.getDefaultValue());
    }

    @Test
    public void resetLastValue() throws Exception {
        gen0Options.resetLastValue(); //should still be callable, it just does nothing if no options are present
        String gen1OptionValue = gen1Option.generateValue();
        String gen2OptionValue = gen2Options.generateValue();
        String gen10OptionValue = gen10Options.generateValue();
        for (int i = 0; i < generationCounter; i++) {
            gen1Option.resetLastValue();
            gen2Options.resetLastValue();
            gen10Options.resetLastValue();
            Assert.assertEquals(gen1OptionValue, gen1Option.generateValue());
            Assert.assertEquals(gen2OptionValue, gen2Options.generateValue());
            Assert.assertEquals(gen10OptionValue, gen10Options.generateValue());
            // generate more values in between just to make sure it also works after two generated values, maybe randomize the number of generated values?
            gen1Option.generateValue();
            gen2Options.generateValue();
            gen10Options.generateValue();
            gen1OptionValue = gen1Option.generateValue();
            gen2OptionValue = gen2Options.generateValue();
            gen10OptionValue = gen10Options.generateValue();
        }
    }

    @Test
    public void getTotalDistinctValues() throws Exception {
        Assert.assertEquals(0, gen0Options.getTotalDistinctValues());
        Assert.assertEquals(1, gen1Option.getTotalDistinctValues());
        Assert.assertEquals(2, gen2Options.getTotalDistinctValues());
        Assert.assertEquals(10, gen10Options.getTotalDistinctValues());
    }

    @Test
    public void getNextDistinctValues() throws Exception {
        Assert.assertEquals(0, gen0Options.getNextDistinctValues());
        Assert.assertEquals(1, gen1Option.getNextDistinctValues());
        Assert.assertEquals(2, gen2Options.getNextDistinctValues());
        Assert.assertEquals(10, gen10Options.getNextDistinctValues());
    }

}