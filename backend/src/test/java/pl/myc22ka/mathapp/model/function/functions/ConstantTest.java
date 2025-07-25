package pl.myc22ka.mathapp.model.function.functions;

import org.junit.jupiter.api.Test;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.exceptions.ServerError;
import pl.myc22ka.mathapp.exceptions.ServerErrorMessages;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.utils.math.MathRandom;

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
        assertEquals(FunctionType.CONSTANT, constant.getType(), "Function type should be CONSTANT");
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
        Exception exception = assertThrows(ServerError.class, () -> {
            new Constant("null");
        });

        assertEquals(ServerErrorMessages.NULL_VALUE_NOT_ALLOWED.toString(), exception.getMessage());
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
    void testMinusWithConstant() {
        var n1 = 4.0;
        var n2 = 5.0;

        Constant c1 = new Constant(F.num(n1));
        Constant c2 = new Constant(F.num(n2));

        Function result = c1.minus(c2);

        assertEquals(n1 + "-(" + n2 + ")", result.getRawExpression());
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

    @Test
    void testDivideByZeroThrowsException() {
        Constant c1 = new Constant(F.ZZ(5));
        Constant zero = new Constant(F.ZZ(0));

        Exception exception = assertThrows(ServerError.class, () -> {
            c1.divide(zero);
        });

        assertEquals(ServerErrorMessages.ILLOGICAL_MATH_OPERATION.toString(), exception.getMessage());
    }

    @Test
    void compositionOfTwoConstantsShouldThrowException() {
        // GIVEN
        Constant c1 = new Constant(org.matheclipse.core.expression.F.num(5));
        Constant c2 = new Constant(org.matheclipse.core.expression.F.num(10));

        // WHEN & THEN
        ServerError exception = assertThrows(ServerError.class, () -> {
            c1.composition(c2);
        });

        assertEquals(ServerErrorMessages.ILLOGICAL_MATH_OPERATION.toString(), exception.getMessage());
    }

    @Test
    void testGetRealRootsForConstantNoSolutions() {
        Constant c1 = new Constant(F.ZZ(5));
        ISymbol symbol = F.Dummy("a");

        var exception1 = assertThrows(ServerError.class, c1::getRealRoots);

        var exception2 = assertThrows(ServerError.class, () ->
                c1.getRealConditionRoots(symbol)
        );

        assertEquals(ServerErrorMessages.NO_SOLUTIONS.toString(), exception1.getMessage());
        assertEquals(ServerErrorMessages.NO_SOLUTIONS.toString(), exception2.getMessage());
    }

    @Test
    void testGetRealRootsForConstantAllSolutions() {
        Constant c1 = new Constant(F.ZZ(0));
        ISymbol symbol = F.Dummy("a");

        var exception1 = assertThrows(ServerError.class, c1::getRealRoots);

        ServerError exception2 = assertThrows(ServerError.class, () ->
                c1.getRealConditionRoots(symbol)
        );

        assertEquals(ServerErrorMessages.ALL_SOLUTIONS.toString(), exception1.getMessage());
        assertEquals(ServerErrorMessages.ALL_SOLUTIONS.toString(), exception2.getMessage());
    }

    @Test
    void testGetDerivativeForConstant(){
        Constant c1 = new Constant(F.ZZ(10));

        Function result = new Function(c1.getDerivative());

        assertEquals("0", result.toString(), "Derivative from constant function should be 0");
    }

    @Test
    void testGetRangeForConstant(){
        Constant c1 = new Constant(F.ZZ(10));

        var range = c1.getRange();

        assertEquals("y∈ℝ", range.toString(), "Derivative from constant function should be 0");
    }

//    @Test
//    void testGetIntegralForConstant(){
//        var c1 = new Linear(F.ZZ(10), F.ZZ(2));
//
//        Function integral = new Function(c1.getIntegral());
//
//        assertEquals("10*x+C", integral.toString(), "Derivative from constant function should be 0");
//    }
}
