import grammar.Terminal;
import org.junit.jupiter.api.Test;
import parser.Tree;

import java.util.List;

public class CalculatorTest extends AbstractTest {
    @Test
    public void test_01_constant_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_02_constant_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42");
        assert(tree.synthesizedAttr.get("e_0.result").equals("42"));
    }


    @Test
    public void test_03_addition_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 + 42");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_04_addition_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 + 42");
        assert(tree.synthesizedAttr.get("e_0.result").equals("84"));
    }

    @Test
    public void test_05_compound_addition_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 + (1 + 2) + 999");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("(", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true),
                        new Terminal(")", false),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_06_compound_addition_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 + (1 + 2) + 999");
        assert(tree.synthesizedAttr.get("e_0.result").equals("1044"));
    }


    @Test
    public void test_07_subtraction_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 - 42");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("-", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_08_subtraction_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 - 43");
        assert(tree.synthesizedAttr.get("e_0.result").equals("-1"));
    }

    @Test
    public void test_09_compound_subtraction_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 - (1 - 2) - 999");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("-", false),
                        new Terminal("(", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("-", false),
                        new Terminal("[0-9]+", true),
                        new Terminal(")", false),
                        new Terminal("-", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_10_compound_subtraction_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 - (1 - 2) - 999");
        assert(tree.synthesizedAttr.get("e_0.result").equals("-956"));
    }


    @Test
    public void test_11_addition_priority_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 + 1 - 2 - 3 + 55 - (1 + 2 - 2 - 1) - 2 - (1 + 1)");
        assert(tree.synthesizedAttr.get("e_0.result").equals("89"));
    }


    @Test
    public void test_12_multiplication_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "11 * 20");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("*", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_13_multiplication_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "11 * 20");
        assert(tree.synthesizedAttr.get("e_0.result").equals("220"));
    }

    @Test
    public void test_14_compound_multiplication_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 * (1 + 2) * 3 + 47 - 3 * 4");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("*", false),
                        new Terminal("(", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true),
                        new Terminal(")", false),
                        new Terminal("*", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("-", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("*", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_15_compound_multiplication_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 * (1 + 2) * 3 + 47 - 3 * 4");
        assert(tree.synthesizedAttr.get("e_0.result").equals("413"));
    }


    @Test
    public void test_16_division_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "440 / 30");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("/", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_17_division_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "440 / 30");
        assert(tree.synthesizedAttr.get("e_0.result").equals("14"));
    }

    @Test
    public void test_18_compound_division_grammar() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 / (1 + 2) * 3 + 47 - 3 * 4 / 6");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("[0-9]+", true),
                        new Terminal("/", false),
                        new Terminal("(", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true),
                        new Terminal(")", false),
                        new Terminal("*", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("+", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("-", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("*", false),
                        new Terminal("[0-9]+", true),
                        new Terminal("/", false),
                        new Terminal("[0-9]+", true)
                )
        ));
    }

    @Test
    public void test_19_compound_division_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "42 / (1 + 2) * 3 + 47 - 3 * 4 / 6");
        assert(tree.synthesizedAttr.get("e_0.result").equals("87"));
    }


    @Test
    public void test_20_multiplication_priority_correctness() {
        Tree tree = parse("sample/Calculator.grammar", "e", "4292 * 1 / 32 / 3 + 535 * (1 * 2 - 200 * 1) / 2 / (1 * 100)");
        assert(tree.synthesizedAttr.get("e_0.result").equals("-485"));
    }


    @Test
    public void test_21_no_whitespaces_lexer_checking() {
        Tree tree = parse("sample/Calculator.grammar", "e", "4292*1/32/3+535*(1*2-200*1)/2/(1*100)");
        assert(tree.synthesizedAttr.get("e_0.result").equals("-485"));
    }

    @Test
    public void test_22_trailing_and_odd_whitespaces_lexer_checking() {
        Tree tree = parse("sample/Calculator.grammar", "e", "   4292   *  1  /  32 /   3 + 535 *( 1        *2  -200         *1) / 2/ (1*   100)  ");
        assert(tree.synthesizedAttr.get("e_0.result").equals("-485"));
    }

    @Test
    public void test_23_incorrect_whitespaces_in_constants_lexer_checking() {
        try {
            parse("sample/Calculator.grammar", "e", "4292 * 1 / 32 / 3 + 53 5 * (1 * 2 - 200 * 1) / 2 / (1 * 100)");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_24_simpleFactorial() {
        Tree tree = parse("sample/Calculator.grammar", "e", "5!");
        assert(tree.synthesizedAttr.get("e_0.result").equals("120"));
    }

    @Test
    public void test_25_arithmeticFactorial() {
        Tree tree = parse("sample/Calculator.grammar", "e", "1 - 3! - 2");
        assert(tree.synthesizedAttr.get("e_0.result").equals("-7"));
    }

    @Test
    public void test_26_expressionFactorial() {
        Tree tree = parse("sample/Calculator.grammar", "e", "(2 * 2! + 1)! - 120");
        assert(tree.synthesizedAttr.get("e_0.result").equals("0"));
    }

    @Test
    public void test_27_multipleFactorial() {
        Tree tree = parse("sample/Calculator.grammar", "e", "3!!");
        assert(tree.synthesizedAttr.get("e_0.result").equals("720"));
    }

    @Test
    public void test_28_multipleExpressionFactorial() {
        Tree tree = parse("sample/Calculator.grammar", "e", "2!!!!!!!!!!!!");
        assert(tree.synthesizedAttr.get("e_0.result").equals("2"));
    }
}
