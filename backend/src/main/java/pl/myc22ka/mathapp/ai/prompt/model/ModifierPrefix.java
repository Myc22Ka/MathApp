package pl.myc22ka.mathapp.ai.prompt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModifierPrefix {
    DIFFICULTY("D"),
    REQUIREMENT("R"),
    TEMPLATE("T");

    private final String key;
}
