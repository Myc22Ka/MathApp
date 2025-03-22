package pl.myc22ka.mathapp.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {
    private ExprEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new ExprEvaluator();
    }

    @Test
    void extractRootsFromExpr_LinearEquation_ReturnsCorrectRoots() {
        // x = 5
        IExpr result = evaluator.eval("Solve[x == 5, x]");

        List<String> roots = MathUtils.extractRootsFromExpr(result);

        assertEquals(1, roots.size());
        assertEquals("5", roots.get(0));
    }

    @Test
    void extractRootsFromExpr_QuadraticEquationWithRealRoots_ReturnsCorrectRoots() {
        // x^2 - 4 = 0 => x = -2, x = 2
        IExpr result = evaluator.eval("Solve[x^2 - 4 == 0, x]");

        List<String> roots = MathUtils.extractRootsFromExpr(result);

        assertEquals(2, roots.size());
        assertTrue(roots.contains("-2"));
        assertTrue(roots.contains("2"));
    }

    @Test
    void extractRootsFromExpr_QuadraticEquationWithComplexRoots_ReturnsCorrectRoots() {
        // x^2 + 1 = 0 => x = -i, x = i
        IExpr result = evaluator.eval("Solve[x^2 + 1 == 0, x]");

        List<String> roots = MathUtils.extractRootsFromExpr(result);

        assertEquals(2, roots.size());
        assertTrue(roots.contains("-I"));
        assertTrue(roots.contains("I"));
    }

    @Test
    void extractRootsFromExpr_CubicEquation_ReturnsCorrectRoots() {
        // x^3 - x = 0 => x = -1, x = 0, x = 1
        IExpr result = evaluator.eval("Solve[x^3 - x == 0, x]");

        List<String> roots = MathUtils.extractRootsFromExpr(result);

        assertEquals(3, roots.size());
        assertTrue(roots.contains("-1"));
        assertTrue(roots.contains("0"));
        assertTrue(roots.contains("1"));
    }

    @Test
    void extractRootsFromExpr_EquationWithComplexCoefficients_ReturnsCorrectRoots() {
        // x^2 + 2*I*x + 5 = 0
        IExpr result = evaluator.eval("Solve[x^2 + 2*I*x + 5 == 0, x]");

        List<String> roots = MathUtils.extractRootsFromExpr(result);

        assertEquals(2, roots.size());
        // Results vary by implementation, so check format rather than exact values

        System.out.println(roots);
        for (String root : roots) {
            assertFalse(root.charAt(0) == '(' && root.charAt(root.length() - 1) == ')');
        }
    }

    @Test
    void extractRootsFromExpr_EmptyExpression_ReturnsEmptyList() {
        IExpr emptyExpr = evaluator.eval("{}");

        List<String> roots = MathUtils.extractRootsFromExpr(emptyExpr);

        assertTrue(roots.isEmpty());
    }

    @Test
    void extractRootsFromExpr_NonListExpression_ReturnsEmptyList() {
        IExpr nonListExpr = evaluator.eval("42");

        List<String> roots = MathUtils.extractRootsFromExpr(nonListExpr);

        assertTrue(roots.isEmpty());
    }

    @Test
    void extractRootsFromExpr_RemovesParentheses() {
        IExpr result = evaluator.eval("Solve[x^2 + 1 == 0, x]");

        List<String> roots = MathUtils.extractRootsFromExpr(result);

        assertEquals(2, roots.size());

        for (String root : roots) {
            assertFalse(root.startsWith("("));
            assertFalse(root.endsWith(")"));
        }

        assertTrue(roots.contains("I") || roots.contains("I*1.0"));
        assertTrue(roots.contains("-I") || roots.contains("-I*1.0"));
    }
}