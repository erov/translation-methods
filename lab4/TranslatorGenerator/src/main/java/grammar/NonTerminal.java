package grammar;

import java.util.Objects;

public class NonTerminal implements GrammarItem {
    private final String value;

    public NonTerminal(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("[NonTerminal: %s]", value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof NonTerminal nonTerminal))
            return false;
        return Objects.equals(value, nonTerminal.value);
    }
}
