package pl.myc22ka.mathapp.ai.prompt.validator.checkers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UtilChecker {

    private static final Pattern FRACTION_PATTERN = Pattern.compile("\\b-?\\d+/\\d+\\b");
    private static final Pattern ROOT_PATTERN = Pattern.compile("(√\\s*\\(?[\\w\\d+\\-*/^ ]+\\)?|sqrt\\s*\\(.*?\\))");


    public boolean containsFraction(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        Matcher matcher = FRACTION_PATTERN.matcher(text);
        return matcher.find();
    }

    public boolean containsRoot(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        return ROOT_PATTERN.matcher(text).find();
    }

    public boolean containsOnlyFractions(String text) {
        if (text == null || text.isBlank()) return false;

        // (), [], {}, + - * / and spaces
        String cleaned = text.replaceAll("[\\s+\\-*/()\\[\\]{}]", "");

        if (!cleaned.matches("([0-9]+/[0-9]+)*")) {
            return false;
        }

        return FRACTION_PATTERN.matcher(text).find();
    }

    public boolean containsOnlyRoots(String text) {
        if (text == null || text.isBlank()) return false;

        String cleaned = text.replaceAll("[\\s+\\-*/()\\[\\]{}]", "");

        String[] tokens = cleaned.split("(?=√)");

        for (String token : tokens) {
            if (token.isBlank()) continue;
            if (!ROOT_PATTERN.matcher(token).matches()) {
                return false;
            }
        }

        return ROOT_PATTERN.matcher(text).find();
    }

    public boolean containsSetOperations(String str) {
        return str.contains("∪") || str.contains("∩") || str.contains("\\");
    }

    public boolean hasMixedComplexity(String str) {
        return str.matches(".*[()\\[\\]{}].*") &&
                (containsSetOperations(str) || containsFraction(str) || containsRoot(str));
    }
}
