package pl.myc22ka.mathapp.step.component.steps.sets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.step.component.helper.StepExecutionHelper;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FindAllIntegers}.
 * Tests execution of the step for finding all integers in an interval.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @see FindAllIntegers
 * @since 30.09.2025
 */
@ExtendWith(MockitoExtension.class)
class FindAllIntegersTest {

    @Mock
    private StepExecutionHelper helper;

    @InjectMocks
    private FindAllIntegers findAllIntegers;

    private StepWrapper step;
    private List<ContextRecord> context;

    /**
     * Initializes test data before each test.
     */
    @BeforeEach
    void setUp() {
        step = mock(StepWrapper.class);
        context = new ArrayList<>();
    }

    /**
     * Test executing with a valid finite interval.
     */
    @Test
    void testExecuteWithValidInterval() {
        Interval interval = new Interval("IntervalData({1,LessEqual,Less,5})"); // [1,5)
        List<ISet> sets = List.of(interval);

        when(helper.getSetsFromContext(step, context)).thenReturn(sets);
        when(helper.nextContextKey(context)).thenReturn("result");

        findAllIntegers.execute(step, context);

        assertFalse(context.isEmpty());
        assertEquals("result", context.getFirst().key().templateString());
        assertEquals("{1,2,3,4}", context.getFirst().value());
    }

    /**
     * Test executing with an interval containing infinity.
     * Expects IllegalArgumentException to be thrown.
     */
    @Test
    void testExecuteWithIntervalContainingInfinity() {
        Interval interval = new Interval("IntervalData({1,LessEqual,Less,Infinity})"); // [1, ∞)
        List<ISet> sets = List.of(interval);

        when(helper.getSetsFromContext(step, context)).thenReturn(sets);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> findAllIntegers.execute(step, context));

        assertTrue(ex.getMessage().contains("Cannot find all integers in a range with infinity"));
    }

    /**
     * Test that executing adds a new prefix value with all integers in the interval.
     */
    @Test
    void testExecuteAddsNewPrefixValue() {
        Interval interval = new Interval("IntervalData({3,LessEqual,LessEqual,7})"); // [3,7]
        List<ISet> sets = List.of(interval);

        when(helper.getSetsFromContext(step, context)).thenReturn(sets);
        when(helper.nextContextKey(context)).thenReturn("ints");

        findAllIntegers.execute(step, context);

        assertEquals(1, context.size());
        assertEquals("ints", context.getFirst().key().templateString());
        assertEquals("{3,4,5,6,7}", context.getFirst().value());
    }
}
