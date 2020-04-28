import org.junit.Test;
import static org.junit.Assert.*;

public class ArithmeticTests {

    @Test
    public void canDoAllArithmeticOperations() {
        assertEquals("3", EvaluateArithmeticAction.evaluate("1 + 2"));
        assertEquals("-1", EvaluateArithmeticAction.evaluate("1 - 2"));
        assertEquals("6", EvaluateArithmeticAction.evaluate("3 * 2"));
        assertEquals("2", EvaluateArithmeticAction.evaluate("4 / 2"));
    }

    @Test
    public void canOmitWhitespace() {
        assertEquals("3", EvaluateArithmeticAction.evaluate("1+2"));
        assertEquals("-1", EvaluateArithmeticAction.evaluate("1-2"));
        assertEquals("6", EvaluateArithmeticAction.evaluate("3*2"));
        assertEquals("2", EvaluateArithmeticAction.evaluate("4/2"));
    }

    @Test
    public void canHaveLeadingAndTrailingWhitespace() {
        assertEquals("3", EvaluateArithmeticAction.evaluate(" 1+2 "));
        assertEquals("3", EvaluateArithmeticAction.evaluate("      \n      1+2  \n  \t"));
    }

    @Test
    public void canHaveInternalNewlines() {
        assertEquals("3", EvaluateArithmeticAction.evaluate("1+\n2"));
    }

    @Test
    public void canUseDecimalsInInput() {
        assertEquals("3", EvaluateArithmeticAction.evaluate("1.5 * 2"));
    }

    @Test
    public void canGetDecimalAnswers() {
        // We do not guarantee any particular precision, just check that it has at least, say,  5 places
        assertTrue(EvaluateArithmeticAction.evaluate("2 / 3").startsWith("0.66666"));
    }

    @Test
    public void precendenceWorks() {
        assertEquals("7", EvaluateArithmeticAction.evaluate("1 + 2 * 3"));
        assertEquals("9", EvaluateArithmeticAction.evaluate("(1 + 2) * 3"));
    }

    @Test
    public void canAppendResultToExpression() {
        assertEquals("1 + 1 =2", EvaluateArithmeticAction.evaluate("1 + 1 ="));
        // add whitespace after "="
        assertEquals("5 * 5 = 25", EvaluateArithmeticAction.evaluate("5 * 5 = "));
        assertEquals("5\t*\t5\t=\n25", EvaluateArithmeticAction.evaluate("5\t*\t5\t=\n"));
    }

    @Test
    public void doesNotEvaluateWrongExpression() {
        assertEquals("1 + 1 = X", EvaluateArithmeticAction.evaluate("1 + 1 = X"));
        assertEquals("2 * 2 X", EvaluateArithmeticAction.evaluate("2 * 2 X"));
    }


    @Test
    public void doesNotEvaluateMultipleExpressions() {
        assertEquals("1 + 2 3 + 4", EvaluateArithmeticAction.evaluate("1 + 2 3 + 4"));
        assertEquals("1 + 2\n3 + 4", EvaluateArithmeticAction.evaluate("1 + 2\n3 + 4"));
        assertEquals("1 + 2 = 3 + 4", EvaluateArithmeticAction.evaluate("1 + 2 = 3 + 4"));
    }
}