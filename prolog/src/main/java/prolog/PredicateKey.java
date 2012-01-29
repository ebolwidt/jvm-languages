package prolog;

public class PredicateKey {

    private String name;
    private int arity;

    public PredicateKey(String name, int arity) {
        if (name == null)
            throw new NullPointerException("name");
        this.name = name;
        this.arity = arity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        PredicateKey other = (PredicateKey) o;
        return arity == other.arity && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ arity;
    }

    @Override
    public String toString() {
        return name + "/" + arity;
    }

    public String getName() {
        return name;
    }

    public int getArity() {
        return arity;
    }
}
