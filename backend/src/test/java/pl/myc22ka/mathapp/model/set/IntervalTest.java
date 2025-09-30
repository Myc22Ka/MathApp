package pl.myc22ka.mathapp.model.set;

import org.junit.jupiter.api.Test;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.sets.Interval;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.CLOSED;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.OPEN;

class IntervalTest {

    private final ExprEvaluator evaluator = new ExprEvaluator();

    @Test
    void testClosedIntervalContainsIntegers() {
        Interval interval = new Interval(F.ZZ(1), CLOSED, CLOSED, F.ZZ(4));

        var result = interval.findAllIntegers();

        assertEquals("{1,2,3,4}", result.toString());
    }

    @Test
    void testOpenIntervalExcludesBounds() {
        IExpr start = evaluator.eval("1");
        IExpr end = evaluator.eval("5");
        Interval interval = new Interval(start, OPEN, OPEN, end);

        var result = interval.findAllIntegers();

        assertEquals("{2,3,4}", result.toString());
    }

    @Test
    void testHalfOpenLeftClosedRightOpen() {
        IExpr start = evaluator.eval("0");
        IExpr end = evaluator.eval("3");
        Interval interval = new Interval(start, CLOSED, OPEN, end);

        var result = interval.findAllIntegers();

        assertEquals("{0,1,2}", result.toString());
    }

    @Test
    void testHalfOpenLeftOpenRightClosed() {
        IExpr start = evaluator.eval("0");
        IExpr end = evaluator.eval("3");
        Interval interval = new Interval(start, OPEN, CLOSED, end);

        var result = interval.findAllIntegers();

        assertEquals("{1,2,3}", result.toString());
    }

    @Test
    void testEmptyInterval() {
        IExpr start = evaluator.eval("2");
        IExpr end = evaluator.eval("2");
        Interval interval = new Interval(start, OPEN, OPEN, end);

        var result = interval.findAllIntegers();

        assertEquals("∅", result.toString());
    }

    @Test
    void testNegativeBounds() {
        IExpr start = evaluator.eval("-3");
        IExpr end = evaluator.eval("2");
        Interval interval = new Interval(start, CLOSED, CLOSED, end);

        var result = interval.findAllIntegers();

        assertEquals("{-3,-2,-1,0,1,2}", result.toString());
    }
}

