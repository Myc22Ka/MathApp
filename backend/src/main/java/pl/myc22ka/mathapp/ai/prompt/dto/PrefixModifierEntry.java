package pl.myc22ka.mathapp.ai.prompt.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;
import java.util.Objects;

public record PrefixModifierEntry(TemplatePrefix prefix, String index, List<ModifierRequest> modifiers) {

    public static boolean areEqualLists(@NotNull List<PrefixModifierEntry> list1, @NotNull List<PrefixModifierEntry> list2) {
        if (list1.size() != list2.size()) return false;

        for (int i = 0; i < list1.size(); i++) {
            PrefixModifierEntry e1 = list1.get(i);
            PrefixModifierEntry e2 = list2.get(i);

            if (!Objects.equals(e1.prefix(), e2.prefix())) return false;
            if (!Objects.equals(e1.index(), e2.index())) return false;

            List<ModifierRequest> mods1 = e1.modifiers();
            List<ModifierRequest> mods2 = e2.modifiers();
            if (mods1.size() != mods2.size()) return false;

            for (int j = 0; j < mods1.size(); j++) {
                ModifierRequest m1 = mods1.get(j);
                ModifierRequest m2 = mods2.get(j);

                if (!Objects.equals(m1.getType(), m2.getType())) return false;
                if (!Objects.equals(m1.getDifficultyLevel(), m2.getDifficultyLevel())) return false;
                if (!Objects.equals(m1.getRequirement(), m2.getRequirement())) return false;
                if (!Objects.equals(m1.getTemplate(), m2.getTemplate())) return false;
                if (!Objects.equals(m1.getTemplateInformation(), m2.getTemplateInformation())) return false;
            }
        }
        return true;
    }
}