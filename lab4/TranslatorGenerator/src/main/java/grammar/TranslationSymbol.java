package grammar;

import java.util.Objects;

public class TranslationSymbol implements GrammarItem {
    private final String value;

    public TranslationSymbol(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("[TranslationSymbol: %s]", value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TranslationSymbol translationSymbol))
            return false;
        return Objects.equals(value, translationSymbol.value);
    }
}
