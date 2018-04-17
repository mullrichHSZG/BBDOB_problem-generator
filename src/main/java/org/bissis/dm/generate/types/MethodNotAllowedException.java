package org.bissis.dm.generate.types;

/**
 * Exception that should be thrown if a certain method cannot be used to modify the values of a generator.
 * @author Markus Ullrich
 */
class MethodNotAllowedException extends Exception {

    MethodNotAllowedException(String message) {
        super(message);
    }

}
