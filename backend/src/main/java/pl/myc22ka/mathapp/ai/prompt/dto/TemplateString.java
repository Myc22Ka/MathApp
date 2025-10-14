package pl.myc22ka.mathapp.ai.prompt.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

public record TemplateString(String templateString, TemplatePrefix prefix) {

    @NotNull
    public static TemplateString fromPrefix(@NotNull TemplatePrefix prefix) {
        String generated = prefix.getKey() + "1";
        return new TemplateString(generated, prefix);
    }
}
