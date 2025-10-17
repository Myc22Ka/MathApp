package pl.myc22ka.mathapp.model.set.utils.checker;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.resolver.validator.UtilChecker;
import pl.myc22ka.mathapp.model.set.ISet;

import static pl.myc22ka.mathapp.model.set.ISetType.*;

/**
 * The type Set checker.
 * <p>
 * Provides methods to validate the complexity or difficulty level of
 * mathematical sets represented by the ISet interface.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class SetChecker {

    private final UtilChecker utilChecker;

    /**
     * Checks if the given set meets difficulty level 1 criteria.
     * Level 1 corresponds to sets of type INTERVAL.
     *
     * @param set the set to check
     * @return true if the set is an interval, false otherwise
     */
    public boolean checkDifficultyLevel1(@NotNull ISet set){
        return set.getISetType() == INTERVAL;
    }

    /**
     * Checks if the given set meets difficulty level 2 criteria.
     * Level 2 allows sets that are either INTERVAL or FINITE.
     *
     * @param set the set to check
     * @return true if the set is interval or finite, false otherwise
     */
    public boolean checkDifficultyLevel2(@NotNull ISet set){
        return checkDifficultyLevel1(set) || set.getISetType() == FINITE;
    }

    /**
     * Checks if the given set meets difficulty level 3 criteria.
     * Level 3 allows sets that are INTERVAL, FINITE, or FUNDAMENTAL.
     *
     * @param set the set to check
     * @return true if the set is interval, finite, or fundamental, false otherwise
     */
    public boolean checkDifficultyLevel3(@NotNull ISet set){
        return checkDifficultyLevel2(set) || set.getISetType() == FUNDAMENTAL;
    }

    /**
     * Checks if the given set meets difficulty level 4 criteria.
     * Level 4 includes sets from level 3 that also contain either fractions or roots.
     *
     * @param set the set to check
     * @return true if the set is level 3 and contains fractions or roots, false otherwise
     */
    public boolean checkDifficultyLevel4(@NotNull ISet set) {
        String str = set.toString();
        return checkDifficultyLevel3(set) &&
                (utilChecker.containsFraction(str) || utilChecker.containsRoot(str));
    }

    /**
     * Checks if the given set meets difficulty level 5 criteria.
     * Level 5 includes level 4 sets that additionally contain set operations.
     *
     * @param set the set to check
     * @return true if the set is level 4 and contains set operations, false otherwise
     */
    public boolean checkDifficultyLevel5(@NotNull ISet set) {
        String str = set.toString();
        return checkDifficultyLevel4(set) &&
                utilChecker.containsSetOperations(str);
    }

    /**
     * Checks if the given set meets difficulty level 6 criteria.
     * Level 6 includes level 5 sets that also have mixed complexity,
     * meaning the expression contains brackets combined with fractions,
     * roots, or set operations.
     *
     * @param set the set to check
     * @return true if the set is level 5 and has mixed complexity, false otherwise
     */
    public boolean checkDifficultyLevel6(@NotNull ISet set) {
        String str = set.toString();
        return checkDifficultyLevel5(set) &&
                utilChecker.hasMixedComplexity(str);
    }
}
