package pl.myc22ka.mathapp.ai.prompt.dto;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

public record ContextRecord(TemplateString key, String value) {

    public ContextRecord(String key, TemplatePrefix prefix, String value) {
        this(new TemplateString(key, prefix), value);
    }

    public ContextRecord(TemplatePrefix prefix, String value) {
        this(TemplateString.fromPrefix(prefix), value);
    }
}