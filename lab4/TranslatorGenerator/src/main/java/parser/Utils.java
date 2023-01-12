package parser;

import lexicalAnalyzer.ParseException;

import java.math.BigInteger;
import java.util.Map;

public class Utils {

    public static void call(final String translationSymbol,
                            final Map<String, String> inhAttr,
                            final Map<String, String> synthesizedAttr,
                            final String synthesizedPrefix) throws ParseException {
        switch (translationSymbol) {
            case "$ADD" -> $ADD(inhAttr, synthesizedAttr, synthesizedPrefix);
            case "$SUB" -> $SUB(inhAttr, synthesizedAttr, synthesizedPrefix);
            case "$MUL" -> $MUL(inhAttr, synthesizedAttr, synthesizedPrefix);
            case "$DIV" -> $DIV(inhAttr, synthesizedAttr, synthesizedPrefix);
            case "$FACT" -> $FACT(inhAttr, synthesizedAttr, synthesizedPrefix);
            default ->
                    throw new ParseException(String.format("Unexpected translation symbol found: '%s'", translationSymbol));
        }
    }

    public static void $ADD(final Map<String, String> inhAttr, final Map<String, String> synthesizedAttr, final String synthesizedPrefix) {
        String result = String.valueOf(Integer.parseInt(inhAttr.get("$ADD.op0")) + Integer.parseInt(inhAttr.get("$ADD.op1")));
        synthesizedAttr.put(synthesizedPrefix + ".result", result);
    }

    public static void $SUB(final Map<String, String> inhAttr, final Map<String, String> synthesizedAttr, final String synthesizedPrefix) {
        String result = String.valueOf(Integer.parseInt(inhAttr.get("$SUB.op0")) - Integer.parseInt(inhAttr.get("$SUB.op1")));
        synthesizedAttr.put(synthesizedPrefix + ".result", result);
    }

    public static void $MUL(final Map<String, String> inhAttr, final Map<String, String> synthesizedAttr, final String synthesizedPrefix) {
        String result = String.valueOf(Integer.parseInt(inhAttr.get("$MUL.op0")) * Integer.parseInt(inhAttr.get("$MUL.op1")));
        synthesizedAttr.put(synthesizedPrefix + ".result", result);
    }

    public static void $DIV(final Map<String, String> inhAttr, final Map<String, String> synthesizedAttr, final String synthesizedPrefix) {
        String result = String.valueOf(Integer.parseInt(inhAttr.get("$DIV.op0")) / Integer.parseInt(inhAttr.get("$DIV.op1")));
        synthesizedAttr.put(synthesizedPrefix + ".result", result);
    }

    public static void $FACT(final Map<String, String> inhAttr, final Map<String, String> synthesizedAttr, final String synthesizedPrefix) {
        final int bound = Integer.parseInt(inhAttr.get("$FACT.op0"));
        BigInteger result = BigInteger.valueOf(1);
        for (int i = 2; i <= bound; ++i) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        synthesizedAttr.put(synthesizedPrefix + ".result", result.toString());
    }
}
