import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.ServerError;
import java.util.Arrays;
import java.util.List;

public class Python2CTests {
    private void compileC(String filename) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "gcc " + filename);
        try {
            Process process = processBuilder.start();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                System.err.printf("Success of compiling file '%s'!%n", filename);
            } else {
                throw new RuntimeException(String.format("Error occurred while compiling file '%s'%n", filename));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Compiling C program inner error occurred: ", e);
        }
    }

    private void run(String filename) {
        String py = "python/" + filename + ".py";
        String c = "c/" + filename + ".c";
        String[] args = {py, c};
        Main.main(args);
        compileC(c);
    }

    @Test
    public void test_01_primitivesDeclaration() {
        run("primitives");
    }

    @Test
    public void test_02_assignment() {
        run("assignment");
    }

    @Test
    public void test_03_inputReading() {
        run("input");
    }

    @Test
    public void test_04_outputPrinting() {
        run("output");
    }

    @Test
    public void test_05_inputOutput() {
        run("input-output");
    }

    @Test
    public void test_06_intExpression() {
        run("int-expression");
    }

    @Test
    public void test_07_boolExpression() {
        run("bool-expression");
    }

    @Test
    public void test_08_comparison() {
        run("comparison");
    }

    @Test
    public void test_09_simpleIf() {
        run("simple-if");
    }

    @Test
    public void test_10_complicatedIf() {
        run("complicated-if");
    }

    @Test
    public void test_11_ifReentrancy() {
        run("if-reentrancy");
    }

    @Test
    public void test_12_ifElse() {
        run("if-else");
    }

    @Test
    public void test_13_swap() {
        run("swap");
    }

    @Test
    public void test_14_swapComplicated() {
        run("complicated-swap");
    }

}
