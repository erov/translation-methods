package lexicalAnalyzer;

import grammar.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LexicalAnalyzer {
    public final static Terminal END = new Terminal("<$>", false);
    private final Set<Terminal> terminals;
    private final String inputString;
    private int currentPosition;
    private char currentChar;
    private Terminal currentToken;


    public LexicalAnalyzer(final Set<Terminal> terminals, final String inputString) {
        this.terminals = terminals;
        this.inputString = inputString;
        this.currentPosition = 0;
        this.currentChar = '\0';
        this.currentToken = END;
        nextChar();
    }

    public Terminal getToken() {
        return currentToken;
    }

    public int getPosition() {
        return currentPosition - 1;
    }

    public void nextToken() throws ParseException {
//        System.err.printf("'currentPosition: %d, currentChar: %s%n", currentPosition, currentChar);
        StringBuilder stringBuilder = new StringBuilder();

        while (isSpace(currentChar)) {
            nextChar();
        }

//        System.err.printf("currentPosition: %d, currentChar: %s%n", currentPosition, currentChar);
        List<Terminal> previousMatches = new ArrayList<>();
        while (currentChar != '\0' && !isSpace(currentChar)) {
            stringBuilder.append(currentChar);
            nextChar();

            int matchesPrefixes = 0;
            List<Terminal> currentMatches = new ArrayList<>();
            for (Terminal terminal : terminals) {
                if (terminal.isRegexp()) {
                    if (stringBuilder.toString().matches(terminal.getValue())) {
                        ++matchesPrefixes;
                        currentMatches.add(new Terminal(terminal, stringBuilder.toString()));
                    }
                } else {
                    if (terminal.getValue().startsWith(stringBuilder.toString())) {
                        ++matchesPrefixes;
                    }
                    if (terminal.getValue().equals(stringBuilder.toString())) {
                        currentMatches.add(terminal);
                    }
                }
            }

            if (currentMatches.isEmpty() && matchesPrefixes == 0) {
                break;
            }

            previousMatches.addAll(currentMatches);
        }
//        System.err.printf("CurrentChar: %s, current position: %d, sb: %s%n", currentChar, currentPosition, stringBuilder);
//        System.err.println(previousMatches);

        if (previousMatches.isEmpty()) {
            if (currentPosition != inputString.length() + 1) {
                throw new ParseException(String.format("Found unexpected symbol '%s' at position %d", currentChar, currentPosition));
            }
            currentToken = END;
            return;
        }

        Terminal dummy = new Terminal(previousMatches.get(0), "");
        Terminal greedy = dummy;
        for (Terminal terminal : previousMatches) {
            if (terminal.getParsedValue().length() > greedy.getParsedValue().length() && !terminal.isRegexp()) {
                greedy = terminal;
            }
        }
        if (greedy == dummy) {
            for (Terminal terminal : previousMatches) {
                if (terminal.getParsedValue().length() > greedy.getParsedValue().length()) {
                    greedy = terminal;
                }
            }
        }

        for (int i = 0; i < stringBuilder.length() - greedy.getParsedValue().length(); ++i) {
            rollback();
        }

        currentToken = greedy;
    }

    public static void checkLexicalAnalyzer(final Set<Terminal> terminals, final String string) throws ParseException {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(terminals, string);
        while (true) {
            lexicalAnalyzer.nextToken();
            Terminal token = lexicalAnalyzer.getToken();
            System.out.printf("%s ", token);
            if (token == END) {
                break;
            }
        }
        System.out.println();
    }


    private boolean isSpace(char ch) {
        return Character.isWhitespace(ch);
    }

    private void nextChar() {
        if (currentPosition != inputString.length()) {
            currentChar = inputString.charAt(currentPosition);
        } else {
            currentChar = '\0';
        }
        currentPosition++;
    }

    private void rollback() {
        assert(currentPosition != 0);
        if (currentPosition == 1) {
            currentChar = '\0';
            currentPosition = 0;
            return;
        }
        currentChar = inputString.charAt(currentPosition - 2);
        --currentPosition;
    }
}
