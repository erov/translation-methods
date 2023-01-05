package grammar;

import java.util.Objects;

public class Terminal implements GrammarItem {
    private final String value;
    private final boolean isRegexp;
    private final String regexpValue;

    public Terminal(final String value, final boolean isRegexp) {
        this.value = value;
        this.isRegexp = isRegexp;
        this.regexpValue = null;
    }

    public Terminal(final Terminal terminal, final String regexpValue) {
        this.value = terminal.value;
        this.isRegexp = terminal.isRegexp;
        this.regexpValue = regexpValue;
    }

    public String getValue() {
        return value;
    }

    public boolean isRegexp() {
        return isRegexp;
    }

    public String getParsedValue() {
        return isRegexp ? regexpValue : value;
    }

    @Override
    public String toString() {
        if (isRegexp) {
            return String.format("(Terminal, regexp: %s, value: %s)", value, getParsedValue());
        }
        return String.format("(Terminal, '%s')", value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isRegexp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Terminal terminal))
            return false;
        return Objects.equals(value, terminal.value) &&
                Objects.equals(isRegexp, terminal.isRegexp);
    }
}
