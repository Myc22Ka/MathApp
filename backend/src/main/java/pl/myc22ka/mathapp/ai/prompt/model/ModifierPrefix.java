package pl.myc22ka.mathapp.ai.prompt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum ModifierPrefix {
    DIFFICULTY("D"),
    REQUIREMENT("R"),
    TEMPLATE("T");

    private final String key;

    @NotNull
    public static Optional<ModifierPrefix> fromKey(String key) {
        return Arrays.stream(values())
                .filter(p -> p.key.equals(key))
                .findFirst();
    }
}
