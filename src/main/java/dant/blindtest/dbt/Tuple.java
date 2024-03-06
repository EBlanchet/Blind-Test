package dant.blindtest.dbt;

import java.util.Objects;

public class Tuple {

    private final String name;

    private final String value;

    public Tuple(String name, String val) {
        this.name = name;
        this.value = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(name, tuple.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getValue() {
        return this.value;
    }
}
