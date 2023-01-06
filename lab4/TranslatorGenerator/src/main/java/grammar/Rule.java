package grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rule {
    private final NonTerminal lhs;
    private final List<GrammarItem> rhs;
    private final Type type;
    private final Map<String, String> attributeAssignments;



    public Rule(final String lhs, final List<GrammarItem> rhs, final Type type, final Map<String, String> attributeAssignments) {
        this.lhs = new NonTerminal(lhs);
        this.rhs = new ArrayList<>(rhs);
        this.type = type;
        this.attributeAssignments = attributeAssignments;
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

    public Map<String, String> getAttributeAssignments() {
        return attributeAssignments;
    }
}
