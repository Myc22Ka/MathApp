package pl.myc22ka.mathapp.ai.prompt.validator.checkers;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;

@Component
@RequiredArgsConstructor
public class SetChecker {

    public boolean checkDifficultyLevel1(@NotNull ISet set){
        return set.getISetType() == ISetType.INTERVAL;
    }
}
