package org.ead.generate.types;

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
     * @see IGenerateDataType#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGenerateDataType that = (AbstractGenerateDataType) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
