package grammar;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private final NonTerminal lhs;
    private final List<GrammarItem> rhs;
    private final Type type;



    public Rule(final String lhs, final List<GrammarItem> rhs, final Type type) {
        this.lhs = new NonTerminal(lhs);
        this.rhs = new ArrayList<>(rhs);
        this.type = type;
    }

    public NonTerminal getLhs() {
        return lhs;
    }

    public List<GrammarItem> getRhs() {
        return rhs;
    }

    public Type getType() {
        return type;
    }
}
