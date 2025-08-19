package pl.myc22ka.mathapp.ai.prompt.dto;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;

public record PrefixModifierEntry(TemplatePrefix prefix, List<ModifierRequest> modifiers) {}