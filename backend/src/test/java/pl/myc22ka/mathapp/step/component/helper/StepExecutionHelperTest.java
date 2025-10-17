package pl.myc22ka.mathapp.step.component.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.utils.resolver.dto.TemplateString;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepExecutionHelperTest {

    @Mock
    private ExpressionFactory expressionFactory;

    @Mock
    private StepDefinitionRepository stepDefinitionRepository;

    @Mock
    private StepWrapper step;

    private StepExecutionHelper helper;

    @BeforeEach
    void setUp() {
        helper = new StepExecutionHelper(expressionFactory, stepDefinitionRepository);
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingPrefixes() {
        when(step.getPrefixes()).thenReturn(List.of("X"));
        ContextRecord rec = new ContextRecord(new TemplateString("s1", TemplatePrefix.SET), "{1,2}");
        List<ContextRecord> context = List.of(rec);

        List<ISet> result = helper.getSetsFromContext(step, context);

        assertThat(result).isEmpty();
        verifyNoInteractions(expressionFactory);
    }

    @Test
    void shouldReturnSetsWhenMatchingPrefixes() {
        // given
        ISet set1 = mock(ISet.class);
        ISet set2 = mock(ISet.class);

        when(step.getPrefixes()).thenReturn(List.of("s1", "s2"));

        ContextRecord rec1 = new ContextRecord(new TemplateString("s1", TemplatePrefix.SET), "{1,2}");
        ContextRecord rec2 = new ContextRecord(new TemplateString("s2", TemplatePrefix.SET), "{3,4}");
        List<ContextRecord> context = List.of(rec1, rec2);

        when(expressionFactory.parse(rec1)).thenReturn(set1);
        when(expressionFactory.parse(rec2)).thenReturn(set2);

        // when
        List<ISet> result = helper.getSetsFromContext(step, context);

        // then
        assertThat(result).containsExactlyInAnyOrder(set1, set2);
        verify(expressionFactory).parse(rec1);
        verify(expressionFactory).parse(rec2);
    }
}