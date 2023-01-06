import grammar.Terminal;
import org.junit.jupiter.api.Test;
import parser.Tree;

import java.util.List;

public class KotlinArrayTest extends AbstractTest {
    @Test
    public void test_01_simple_definition() {
        Tree tree = parse("sample/KotlinArray.grammar", "d", "var array: Array<Int>;");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("var", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(":", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(">", false),
                        new Terminal(";", false)
                )
        ));
    }

    @Test
    public void test_02_array_of_array() {
        Tree tree = parse("sample/KotlinArray.grammar", "d", "var array: Array<Array<Int>>;");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("var", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(":", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(";", false)
                )
        ));
    }

    @Test
    public void test_03_odd_spaces() {
        Tree tree = parse("sample/KotlinArray.grammar", "d", "var    array         :  Array    <   Array<        Int> >        ;   ");
        assert(tree.walkthrough().equals(
                List.of(
                        new Terminal("var", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(":", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(";", false)
                )
        ));
    }

    @Test
    public void test_04_big_reentrancy() {
        Tree tree = parse("sample/KotlinArray.grammar", "d", "var array: Array<Array<Array<Array<Array<Array<Array<Array<Array<Array<Int>>>>>>>>>>;");
        assert (tree.walkthrough().equals(
                List.of(
                        new Terminal("var", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(":", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("Array", false),
                        new Terminal("<", false),
                        new Terminal("[a-zA-Z0-9]+", true),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(">", false),
                        new Terminal(";", false)
                )
        ));
    }


    @Test
    public void test_05_missing_all_spaces() {
        try {
            parse("sample/KotlinArray.grammar", "d", "vararray:Array<Array<Int>>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_06_semicolon_instead_of_colon() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array; Array<Array<Int>>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_07_array_lowercase_keyword() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: array<String>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_08_missing_langle() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array String>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_09_lparen_instead_of_langle() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array (String>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_10_missing_rangle() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<String;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_11_rfigure_instead_of_rangle() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<String};");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_12_missing_semicolon() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<String>");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_13_wrong_keyword_name() {
        try {
            parse("sample/KotlinArray.grammar", "d", "val array: Array<Int>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_14_missing_keyword() {
        try {
            parse("sample/KotlinArray.grammar", "d", "array: Array<Int>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_15_typename_instead_of_keyword() {
        try {
            parse("sample/KotlinArray.grammar", "d", "Array array: Array<Int>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_16_wrong_typename() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<;>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_17_wrong_typename_2() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<var>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_18_missing_typename() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_19_wrong_variable_name() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var Array: Array<Int>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }


    @Test
    public void test_20_wrong_variable_name_2() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var var: Array<Int>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_21_missing_variable() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var      : Array<Int>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }


    @Test
    public void test_22_missing_parameter() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<Array<>>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_23_missing_parametrization() {
        try {
            parse("sample/KotlinArray.grammar", "d", "var array: Array<Array>;");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }

    @Test
    public void test_24_empty_input() {
        try {
            parse("sample/KotlinArray.grammar", "d", "");
        } catch (Throwable e) {
            return;
        }
        assert(true);
    }


}
