import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

public class ProgramParserListener extends ProgramBaseListener {
    private final StringBuilder stringBuilder = new StringBuilder();
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
        STRING("std::string");

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
                .append("#include <iostream>\n")
                .append("#include <string>\n")
                .append("\n")
                .append("int main(int argc, char** argv) {\n");
    }

    @Override
    public void exitProgram(ProgramParser.ProgramContext ctx) {
        stringBuilder.append("\n}\n\n");
        System.out.println(stringBuilder);
    }

    @Override
    public void enterLine(ProgramParser.LineContext ctx) {
        System.err.println("line");
    }

    @Override
    public void enterIndent(ProgramParser.IndentContext ctx) {
        ++lineNumber;
        int len = (ctx.getChildCount() == 0) ? 0 : ctx.WHITESPACE().getText().length();
        System.err.println(len + " " + tabs);
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
        typeCheck(name, Type.INT);
        if (!isInt(ctx.INT().getText())) {
            throw new TranslatorException("Variable " + name + " should be assigned to [INT_MIN, INT_MAX] bounded value!");
        }
    }

    @Override
    public void enterChar_declaration(ProgramParser.Char_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.CHAR);
    }

    @Override
    public void enterBoolean_declaration(ProgramParser.Boolean_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.BOOL);
    }

    @Override
    public void enterString_declaration(ProgramParser.String_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        typeCheck(name, Type.STRING);
    }

    @Override
    public void enterCustom_declaration(ProgramParser.Custom_declarationContext ctx) {
        String name = ctx.variable_assignment().VARIABLE().getText();
        String another = ctx.VARIABLE().getText();
        if (!declaredVariables.containsKey(another)) {
            throw new TranslatorException("Variable " + another + " haven't been declared yet!");
        }
        typeCheck(name, declaredVariables.get(another));
    }

    @Override
    public void enterInput_declaration(ProgramParser.Input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        typeCheck(name, Type.STRING);
    }

    @Override
    public void exitInput_declaration(ProgramParser.Input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        readVariable(name);
    }

    @Override
    public void enterInt_input_declaration(ProgramParser.Int_input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        typeCheck(name, Type.INT);
    }

    @Override
    public void exitInt_input_declaration(ProgramParser.Int_input_declarationContext ctx) {
        String name = ctx.input_variable_declaration().VARIABLE().getText();
        readVariable(name);
    }

    @Override
    public void enterInt_expression_declaration(ProgramParser.Int_expression_declarationContext ctx) {
        typeCheck(ctx.variable_assignment().VARIABLE().getText(), Type.INT);
    }

    @Override public void enterBoolean_expression_declaration(ProgramParser.Boolean_expression_declarationContext ctx) {
        typeCheck(ctx.variable_assignment().VARIABLE().getText(), Type.BOOL);
    }

    @Override public void enterString_expression_declaration(ProgramParser.String_expression_declarationContext ctx) {
        typeCheck(ctx.variable_assignment().VARIABLE().getText(), Type.STRING);
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
        typeCheck(ctx.VARIABLE().getText(), Type.INT);
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
        typeCheck(ctx.VARIABLE().getText(), Type.BOOL);
    }

    @Override
    public void enterString_variable(ProgramParser.String_variableContext ctx) {
        typeCheck(ctx.VARIABLE().getText(), Type.STRING);
    }

    @Override
    public void enterChar_variable(ProgramParser.Char_variableContext ctx) {
        typeCheck(ctx.VARIABLE().getText(), Type.CHAR);
    }



    @Override public void enterPrint(ProgramParser.PrintContext ctx) {
        stringBuilder.append("std::cout << ");
    }

    @Override public void exitPrint(ProgramParser.PrintContext ctx) {
        stringBuilder.append("std::endl;");
    }

    @Override
    public void exitPrintAny(ProgramParser.PrintAnyContext ctx) {
        stringBuilder.append(" << ");
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
        stringBuilder.append(terminalRenaming.getOrDefault(text, text));
    }


    private static boolean isInt(final String value) {
        if (value.isEmpty()) {
            return false;
        }
        BigInteger bigInteger = new BigInteger(value);
        return BigInteger.valueOf(-2147483648).compareTo(bigInteger) < 1 &&
                bigInteger.compareTo(BigInteger.valueOf(2147483647)) < 1;
    }

    private void typeCheck(final String name, final Type type) {
        if (!declaredVariables.containsKey(name)) {
            declaredVariables.put(name, type);
            stringBuilder
                    .append(type)
                    .append(' ');
        } else {
            if (declaredVariables.get(name) != type) {
                throw new TranslatorException("Variable " + name + " must have type " + type);
            }
        }
    }

    private void readVariable(final String name) {
        stringBuilder
                .append(";\n")
                .append(" ".repeat(tabs + 4))
                .append("std::cin >> ")
                .append(name);
    }
}
