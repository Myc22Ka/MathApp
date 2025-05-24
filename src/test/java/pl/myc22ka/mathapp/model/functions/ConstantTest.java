package pl.myc22ka.mathapp.model.functions;

import org.junit.jupiter.api.Test;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.MathRandom;

import static org.junit.jupiter.api.Assertions.*;

class ConstantTest {

    private final ExprEvaluator evaluator = new ExprEvaluator();

    @Test
    void testConstructorWithValue() {
        // GIVEN
        IExpr value = F.ZZ(7);

        // WHEN
        Constant constant = new Constant(value);

        // THEN
        assertEquals(F.ZZ(7), constant.getSymjaExpression(), "Symja expression should match the provided value");
        assertEquals("7", constant.getRawExpression(),
                "Raw expression should be the string representation of the value");
        assertEquals(FunctionTypes.CONSTANT, constant.getType(), "Function type should be CONSTANT");
    }

    @Test
    void testZeroValue() {
        // GIVEN
        IExpr zero = F.ZZ(0);

        // WHEN
        Constant constant = new Constant(zero);

        // THEN
        assertEquals("0", constant.getRawExpression(), "You should be able to insert 0");
    }

    @Test
    void testEvaluation() {
        // GIVEN
        Constant constant = new Constant(F.num(3.5));

        // WHEN
        IExpr result = evaluator.eval(constant.getSymjaExpression());

        // THEN
        assertEquals(F.num(3.5), result, "Evaluation should return the constant value itself");
    }

    @Test
    void testToString() {
        // GIVEN / WHEN
        Constant constant = new Constant(F.num(42));

        // THEN
        assertTrue(constant.toString().contains("42"), "String representation should contain the constant value");
    }

    @Test
    void testRandomRealNumbers() {
        for (int i = 0; i < 100; i++) {
            IExpr randomReal = MathRandom.generateRandomReal();
            Constant constant = new Constant(randomReal);

            assertEquals(randomReal, constant.getSymjaExpression(),
                    "Symja expression should match the random real input");
        }
    }

    @Test
    void testRandomComplexNumbers() {
        for (int i = 0; i < 100; i++) {
            IExpr randomComplex = MathRandom.generateRandomComplex();
            Constant constant = new Constant(randomComplex);

            assertEquals(randomComplex, constant.getSymjaExpression(),
                    "Symja expression should match the random complex input");
        }
    }

    @Test
    void constructorThrowsExceptionOnNull() {
        // GIVEN / WHEN / THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Constant(null);
        });

        assertEquals("Value cannot be null", exception.getMessage());
    }

    @Test
    void testPlusWithConstant() {
        var n1 = 4.0;
        var n2 = 5.0;

        Constant c1 = new Constant(F.num(n1));
        Constant c2 = new Constant(F.num(n2));

        Function result = c1.plus(c2);

        assertEquals(n1 + "+(" + n2 + ")", result.getRawExpression());
    }

    @Test
    void testTimesWithConstant() {
        var n1 = 4.0;
        var n2 = 5.0;

        Constant c1 = new Constant(F.num(n1));
        Constant c2 = new Constant(F.num(n2));

        Function result = c1.times(c2);

        assertEquals(n1 + "*" + n2, result.getRawExpression());
    }

    @Test
    void testDivideWithConstant() {
        var n1 = 4.0;
        var n2 = 5.0;

        Constant c1 = new Constant(F.num(n1));
        Constant c2 = new Constant(F.num(n2));

        Function result = c1.divide(c2);

        assertEquals(c1 + "/" + c2, result.getRawExpression());
    }

    // @Test
    // void testCompositionWithConstant() {
    // Constant c = new Constant(F.num(7));
    // Function f = new Function("x+1");

    // Function result = c.composition(f);

    // // Oczekiwany efekt zależy od implementacji composition,
    // // więc tu możesz zweryfikować, że wynik nie jest null i jest zgodny z
    // // oczekiwaniami
    // assertNotNull(result);
    // // Możesz tu doprecyzować asercje jeśli znasz dokładny wynik
    // }
}
