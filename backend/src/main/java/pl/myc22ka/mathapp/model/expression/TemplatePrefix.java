package pl.myc22ka.mathapp.model.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum TemplatePrefix {
    SET("s"),
    FUNCTION("f"),
    ARGUMENT("a");

    private final String key;

    private static final Map<String, TemplatePrefix> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(TemplatePrefix::getKey, e -> e));

    @NotNull
    public static Optional<TemplatePrefix> fromKey(String key) {
        return Optional.ofNullable(lookup.get(key));
    }
}