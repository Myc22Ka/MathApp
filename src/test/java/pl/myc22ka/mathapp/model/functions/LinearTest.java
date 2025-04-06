package pl.myc22ka.mathapp.model.functions;

import org.junit.jupiter.api.Test;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.model.FunctionInterface;
import pl.myc22ka.mathapp.utils.proxy.NotFullyImplementedProxy;

import static org.junit.jupiter.api.Assertions.*;

class LinearTest {
    private static final FunctionInterface function = NotFullyImplementedProxy.createProxy(new Linear(F.x), FunctionInterface.class);

    @Test
    void testAllMethodsImplemented() {
//        function.getRealRoots();
    }

    @Test
    void testConstructor_WithVariable() {
        // Given
        ISymbol x = F.Dummy("x"); // Create a symbolic variable

        // When
        Linear linearFunction = new Linear(x);

        // Then
        assertNotNull(linearFunction);
        assertEquals(x, linearFunction.getVariable(), "Variable should be initialized correctly.");
        assertNotNull(linearFunction.getExpression(), "Expression should be set in updateExpression.");
    }

    @Test
    void testConstructorWithCoefficients() {
        // Given
        ISymbol x = F.Dummy("x");
        IExpr coefficient = F.C3;
        IExpr constant = F.C2;

        // When
        Linear linearFunction = new Linear(coefficient, constant, x);

        // Then
        assertNotNull(linearFunction);
        assertEquals(x, linearFunction.getVariable(), "Variable should be initialized correctly.");
        assertEquals(coefficient, linearFunction.getCoefficient(), "Coefficient should be initialized correctly.");
        assertEquals(constant, linearFunction.getConstant(), "Constant should be initialized correctly.");

        var expectedExpression = F.eval("3*x + 2").toString();
        assertEquals(expectedExpression, linearFunction.toString(), "Expression should be correctly formed.");
    }
}