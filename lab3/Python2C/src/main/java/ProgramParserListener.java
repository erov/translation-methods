import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

public class ProgramParserListener extends ProgramBaseListener {
    private final StringBuilder stringBuilder = new StringBuilder();
    private StringBuilder printFormatBuilder = new StringBuilder();
    private StringBuilder printValuesBuilder = new StringBuilder();
    private boolean nowPrinting = false;
    private int tabs = 0;
    private boolean enableElseKeywordThisTab = false;
    private boolean nowIsElseBranch = false;
    private int lastTabsIncrease = -1;
    private int lineNumber = -1;
    private final Map<String, Type> declaredVariables = new HashMap<>();
    private final static Map<String, String> terminalRenaming = Map.ofEntries(
            entry("True", "true"),
            entry("False", "false"),
            entry("=", ""),
            entry("input", ""),
            entry("int", ""),
            entry("print", ""),
            entry(",", ""),
            entry("(", ""),
            entry(")", ""),
            entry("//", "/"),
            entry("or", "||"),
            entry("and", "&&"),
            entry("not", "!"),
            entry(":", "")
    );

    private enum Type {
        INT ("int"),
        BOOL("bool"),
        CHAR("char"),
        STRING("char*");

        private final String name;

        Type(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    @Override
    public void enterProgram(ProgramParser.ProgramContext ctx) {
        stringBuilder
                .append("#include <stdbool.h>\n")
                .append("#include <stdio.h>\n")
                .append("\n")
                .append("int main(int argc, char** argv) {\n");
    }

    @Override
    public void exitProgram(ProgramParser.ProgramContext ctx) {
        stringBuilder.append("\n}\n\n");
        System.out.println(stringBuilder);
    }

    @Override
    public void enterLine(ProgramParser.LineContext ctx) { }

    @Override
    public void enterIndent(ProgramParser.IndentContext ctx) {
        ++lineNumber;
        int len = (ctx.getChildCount() == 0) ? 0 : ctx.WHITESPACE().getText().length();
        enableElseKeywordThisTab = false;
        if (len == tabs) {
            stringBuilder.append(" ".repeat(tabs + 4));
            return;
        }
        if (len < tabs && (tabs - len) % 4 == 0 && lastTabsIncrease + 1 != lineNumber) {
            while (len != tabs) {
                tabs -= 4;
                stringBuilder
                        .append(" ".repeat(tabs + 4))
                        .append("}\n");
            }
            enableElseKeywordThisTab = true;
            nowIsElseBranch = false;
            stringBuilder.append(" ".repeat(tabs + 4));
            return;
        }
        throw new TranslatorException("Unexpected indent size " + len);
    }

    @Override
    public void exitDeclaration(ProgramParser.DeclarationContext ctx) {
        stringBuilder.append(";");
    }

    @Override
    public void enterInt_declaration(ProgramParser.Int_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.INT, false);
        if (!isInt(ctx.INT().getText())) {
            throw new TranslatorException("Variable " + name + " should be assigned to [INT_MIN, INT_MAX] bounded value!");
        }
    }

    @Override
    public void enterChar_declaration(ProgramParser.Char_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.CHAR, false);
    }

    @Override
    public void enterBoolean_declaration(ProgramParser.Boolean_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.BOOL, false);
    }

    @Override
    public void enterString_declaration(ProgramParser.String_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.STRING, false);
    }

    @Override
    public void enterCustom_declaration(ProgramParser.Custom_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        String another = ctx.VARIABLE().getText();
        if (!declaredVariables.containsKey(another)) {
            throw new TranslatorException("Variable " + another + " haven't been declared yet!");
        }
        typeCheck(name, declaredVariables.get(another), false);
    }

    @Override
    public void enterInput_declaration(ProgramParser.Input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        typeCheck(name, Type.STRING, true);
    }

    @Override
    public void exitInput_declaration(ProgramParser.Input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        stringBuilder.append("[256]");
        readVariable(name, Type.STRING);
    }

    @Override
    public void enterInt_input_declaration(ProgramParser.Int_input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        typeCheck(name, Type.INT, false);
    }

    @Override
    public void exitInt_input_declaration(ProgramParser.Int_input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        readVariable(name, Type.INT);
    }

    @Override
    public void enterInt_expression_declaration(ProgramParser.Int_expression_declarationContext ctx) {
        typeCheck(ctx.variable_assignment().VARIABLE().getText(), Type.INT, false);
    }

    @Override public void enterBoolean_expression_declaration(ProgramParser.Boolean_expression_declarationContext ctx) {
        typeCheck(ctx.variable_assignment().VARIABLE().getText(), Type.BOOL, false);
    }

    @Override
    public void enterInt_e_parentheses(ProgramParser.Int_e_parenthesesContext ctx) {
        stringBuilder.append('(');
    }

    @Override public void exitInt_e_parentheses(ProgramParser.Int_e_parenthesesContext ctx) {
        stringBuilder.append(')');
    }

    @Override
    public void enterInt_variable(ProgramParser.Int_variableContext ctx) {
        typeCheck(ctx.VARIABLE().getText(), Type.INT, false);
    }



    @Override
    public void enterBoolean_b_parentheses(ProgramParser.Boolean_b_parenthesesContext ctx) {
        stringBuilder.append('(');
    }

    @Override
    public void exitBoolean_b_parentheses(ProgramParser.Boolean_b_parenthesesContext ctx) {
        stringBuilder.append(')');
    }

    @Override
    public void enterBoolean_variable(ProgramParser.Boolean_variableContext ctx) {
        typeCheck(ctx.VARIABLE().getText(), Type.BOOL, false);
    }



    @Override public void enterPrint(ProgramParser.PrintContext ctx) {
        nowPrinting = true;
        stringBuilder.append("printf(");
    }

    @Override public void exitPrint(ProgramParser.PrintContext ctx) {
        nowPrinting = false;
        stringBuilder
                .append('"')
                .append(printFormatBuilder)
                .append("\\n\"")
                .append(printValuesBuilder)
                .append(");");
        printFormatBuilder = new StringBuilder();
        printValuesBuilder = new StringBuilder();
    }

    @Override
    public void enterPrintAny(ProgramParser.PrintAnyContext ctx) {
        printValuesBuilder.append(", ");
    }

    @Override
    public void enterDeclared_variable(ProgramParser.Declared_variableContext ctx) {
        String name = ctx.VARIABLE().getText();
        if (!declaredVariables.containsKey(name)) {
            throw new TranslatorException("Variable " + name + " must have been already declared!");
        }
    }

    @Override
    public void exitIf_(ProgramParser.If_Context ctx) {
        tabs += 4;
        lastTabsIncrease = lineNumber;
    }

    @Override
    public void enterIf_statement(ProgramParser.If_statementContext ctx) {
        stringBuilder.append('(');
    }

    @Override public void exitIf_statement(ProgramParser.If_statementContext ctx) {
        stringBuilder.append("){");
    }

    @Override public void enterElse_(ProgramParser.Else_Context ctx) {
        if (!enableElseKeywordThisTab) {
            throw new TranslatorException("If keyword may have up to only 1 else-branch");
        }
        tabs += 4;
        lastTabsIncrease = lineNumber;
        nowIsElseBranch = true;
    }

    @Override public void exitElse_(ProgramParser.Else_Context ctx) {
        stringBuilder.append("{");
    }



    @Override
    public void exitVariable_assignment(ProgramParser.Variable_assignmentContext ctx) {
        stringBuilder.append('=');
    }


    @Override
    public void visitTerminal(TerminalNode node) {
        if (Objects.equals(node.getSymbol().getType(), Token.EOF)) {
            return;
        }
        if (node.getSymbol().getType() == ProgramParser.WHITESPACE) {
            return;
        }
        String text = node.getText();
        if (nowPrinting) {
            if (isInt(text) || declaredVariables.containsKey(text) && declaredVariables.get(text) == Type.INT) {
                printFormatBuilder.append("%d ");
                printValuesBuilder.append(text);
            } else if (isBool(text) || declaredVariables.containsKey(text) && declaredVariables.get(text) == Type.BOOL) {
                printFormatBuilder.append("%d ");
                printValuesBuilder.append(text);
            } else if (isChar(text) || declaredVariables.containsKey(text) && declaredVariables.get(text) == Type.CHAR) {
                printFormatBuilder.append("%c ");
                printValuesBuilder.append(text);
            } else if (isString(text) || declaredVariables.containsKey(text) && declaredVariables.get(text) == Type.STRING) {
                printFormatBuilder.append("%s ");
                printValuesBuilder.append(text);
            }
            return;
        }
        stringBuilder.append(terminalRenaming.getOrDefault(text, text));
    }


    private static boolean isInt(final String value) {
        if (value.isEmpty()) {
            return false;
        }
        try {
            BigInteger bigInteger = new BigInteger(value);
            return BigInteger.valueOf(-2147483648).compareTo(bigInteger) < 1 &&
                    bigInteger.compareTo(BigInteger.valueOf(2147483647)) < 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBool(final String value) {
        return value.equals("True") || value.equals("False");
    }

    private static boolean isChar(final String value) {
        return value.length() == 3 && value.charAt(0) == '\'' && value.charAt(2) == '\'';
    }

    private static boolean isString(final String value) {
        return value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"';
    }

    private void typeCheck(final String name, final Type type, final boolean inputString) {
        if (!declaredVariables.containsKey(name)) {
            declaredVariables.put(name, type);
            if (inputString) {
                stringBuilder
                        .append("char")
                        .append(' ');
            } else {
                stringBuilder
                        .append(type)
                        .append(' ');
            }
        } else {
            if (declaredVariables.get(name) != type) {
                throw new TranslatorException("Variable " + name + " must have type " + type);
            }
        }
    }

    private void readVariable(final String name, final Type type) {
        stringBuilder
                .append(";\n")
                .append(" ".repeat(tabs + 4))
                .append("scanf(\"%")
                .append(type == Type.INT ? "d" : "s")
                .append("\", ")
                .append(type == Type.INT ? "&" : "")
                .append(name)
                .append(")");
    }
}
