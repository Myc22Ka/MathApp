package pl.myc22ka.mathapp.ai.prompt.validator;

import org.junit.jupiter.api.Test;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TemplateResolverTest {

    private final ModifierRepository modifierRepository = mock(ModifierRepository.class);
    private final TemplateResolver resolver = new TemplateResolver(modifierRepository);

    @Test
    void shouldFindSinglePrefixWithoutModifiers() {
        // given
        String input = "${s1:T1}";

        // when
        List<PrefixModifierEntry> result = resolver.findPrefixModifiers(input);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().prefix().getKey()).isEqualTo("T");
        assertThat(result.getFirst().modifiers()).isEmpty();
    }

//    @Test
//    void shouldFindSinglePrefixWithOneModifier() {
//        // given
//        String input = "${T1:D1}";
//
//        // when
//        List<PrefixModifierEntry> result = resolver.findPrefixModifiers(input);
//
//        // then
//        assertThat(result).hasSize(1);
//        assertThat(result.getFirst().prefix().getKey()).isEqualTo("T");
//        // na razie puste, ale możesz sprawdzić regex czy wykrywa
//        assertThat(result.getFirst().modifiers()).isEmpty();
//    }
//
//    @Test
//    void shouldFindSinglePrefixWithMultipleModifiers() {
//        // given
//        String input = "${T1:D1|R2|X3}";
//
//        // when
//        List<PrefixModifierEntry> result = resolver.findPrefixModifiers(input);
//
//        // then
//        assertThat(result).hasSize(1);
//        assertThat(result.getFirst().prefix().getKey()).isEqualTo("T");
//        assertThat(result.getFirst().modifiers()).isEmpty();
//    }
//
//    @Test
//    void shouldFindMultiplePrefixes() {
//        // given
//        String input = "Hello ${T1:D1|R2} and ${S2:X1} world";
//
//        // when
//        List<PrefixModifierEntry> result = resolver.findPrefixModifiers(input);
//
//        // then
//        assertThat(result).hasSize(2);
//
//        assertThat(result.get(0).prefix().getKey()).isEqualTo("T");
//        assertThat(result.get(0).modifiers()).isEmpty();
//
//        assertThat(result.get(1).prefix().getKey()).isEqualTo("S");
//        assertThat(result.get(1).modifiers()).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenNoTemplatesFound() {
//        // given
//        String input = "just plain text";
//
//        // when
//        List<PrefixModifierEntry> result = resolver.findPrefixModifiers(input);
//
//        // then
//        assertThat(result).isEmpty();
//    }
}