import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProgramParserListener extends ProgramBaseListener {
    private final StringBuilder stringBuilder = new StringBuilder();
    private int tabs = 0;
    private final Map<String, Type> declaredVariables = new HashMap<>();
    private final static Map<String, String> terminalRenaming = Map.of(
            "True", "true",
            "False", "false",
            "=", "",
            "input()", "",
            "int(input())", "",
            "print(", "",
            ",", "",
            ")", ""
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
        stringBuilder.append(" ".repeat(tabs + 4));
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
        String name = ctx.string_variable_declaration().VARIABLE().getText();
        typeCheck(name, Type.STRING);
    }

    @Override
    public void exitInput_declaration(ProgramParser.Input_declarationContext ctx) {
        String name = ctx.string_variable_declaration().VARIABLE().getText();
        readVariable(name);
    }

    @Override
    public void enterInt_input_declaration(ProgramParser.Int_input_declarationContext ctx) {
        String name = ctx.int_variable_declaration().VARIABLE().getText();
        typeCheck(name, Type.INT);
    }

    @Override
    public void exitInt_input_declaration(ProgramParser.Int_input_declarationContext ctx) {
        String name = ctx.int_variable_declaration().VARIABLE().getText();
        readVariable(name);
    }

    @Override public void enterPrint(ProgramParser.PrintContext ctx) {
        stringBuilder.append("std::cout << ");
    }

    @Override public void exitPrint(ProgramParser.PrintContext ctx) {
        stringBuilder.append("endl;");
    }

    @Override
    public void exitPrintAny(ProgramParser.PrintAnyContext ctx) {
        stringBuilder.append(" << ");
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
            Type originalType = declaredVariables.get(name);
            if (originalType != type) {
                throw new TranslatorException("Variable " + name + " must have type " + originalType);
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
