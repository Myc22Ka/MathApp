package pl.myc22ka.mathapp.model.set.utils.scanner;

/**
 * Stores information about a potential interval found in a string.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public record IntervalInfo(boolean isInterval, int start, int end) {
}