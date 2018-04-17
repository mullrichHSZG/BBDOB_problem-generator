package org.bissis.dm.generate.types;

/**
 * Abstract class for data types.
 * @author Markus Ullrich
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractGenerateDataType implements IGenerateDataType {

    private String name;

    /**
     * The constructor for every data type should ensure, that the type has a name.
     * @param name - the name for this data type
     */
    protected AbstractGenerateDataType(String name) {
        super();
        this.name = name;
    }

    @Override
    public int compareTo(String o) {
        return this.name.compareTo(o);
    }

    /*
     * (non-Javadoc)
     * @see org.bissis.dm.generate.types.IGenerateDataType#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

}
