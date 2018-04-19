package org.bissis.dm.generate.types;

/**
 * Interface that should be implemented by generators that can use pre-shuffled value lists.
 * Usually, the generator will just attempt to create new random values.
 * If that is not possible, due to the values being pre-generated, it can order the generator to re-shuffle the remaining values in the list.
 * @author Markus Ullrich
 */
public interface PreGeneratedListGenerator {

    /**
     * This method can be called to re-shuffle the remaining values in the pre-generated list of this generator.
     */
    void shuffleRemainingValues();

}
