package Scheduler.Utils;

/**
 * Custom implementation of a pair data structure class to facilitate with report generation algorithms
 *
 * @author Chris Criswell
 */
public class StringPair<String> {

    /**
     * First string
     */
    String first;
    /**
     * Second string
     */
    String second;

    /**
     * Constructor to initialize members with values
     * @param first first string
     * @param second second string
     */
    public StringPair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Accessor for first string
     * @return first string of pair
     */
    public String getFirst() { return first; }

    /**
     * Accessor for second string
     * @return second string of pair
     */
    public String getSecond() { return second; }

    /**
     * Mutator for first string
     * @param first first string
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Mutator for second string
     * @param second second string
     */
    public void setSecond(String second) {
        this.second = second;
    }

    //https://www.educative.io/edpresso/what-is-the-hashcode-method-in-java
    /**
     * Override of object hashCode method to allow for StringPair class to be used as key for HashMap
     * @return integer value of hash code derived from class members
     */
    @Override
    public int hashCode() {
        return (int)first.hashCode() + second.hashCode();
    }

    //https://www.educative.io/edpresso/what-is-the-hashcode-method-in-java
    /**
     * Override of object equals method to allow for StringPair class to be used as key for HashMap
     * @param obj object to compare to
     * @return result of equality check
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringPair test = (StringPair)obj;

        if (test.getFirst().equals(this.getFirst()) && test.getSecond().equals(this.getSecond())) {
            return true;
        }

        return false;
    }

}
