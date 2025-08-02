package pl.myc22ka.mathapp.model.set.utils.checker;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.validator.UtilChecker;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;

import static pl.myc22ka.mathapp.model.set.ISetType.*;

@Component
@RequiredArgsConstructor
public class SetChecker {

    private final UtilChecker utilChecker;

    public boolean checkDifficultyLevel1(@NotNull ISet set){
        return set.getISetType() == INTERVAL;
    }

    public boolean checkDifficultyLevel2(@NotNull ISet set){
        return checkDifficultyLevel1(set) || set.getISetType() == FINITE;
    }

    public boolean checkDifficultyLevel3(@NotNull ISet set){
        return checkDifficultyLevel2(set) || set.getISetType() == FUNDAMENTAL;
    }

    public boolean checkDifficultyLevel4(@NotNull ISet set) {
        String str = set.toString();
        return checkDifficultyLevel3(set) &&
                (utilChecker.containsFraction(str) || utilChecker.containsRoot(str));
    }

    public boolean checkDifficultyLevel5(@NotNull ISet set) {
        String str = set.toString();
        return checkDifficultyLevel4(set) &&
                utilChecker.containsSetOperations(str);
    }

    public boolean checkDifficultyLevel6(@NotNull ISet set) {
        String str = set.toString();
        return checkDifficultyLevel5(set) &&
                utilChecker.hasMixedComplexity(str);
    }

    public boolean checkDisjoint(@NotNull ISet set) {
        return set.toString().contains(SetSymbols.UNION.toString());
    }
}
