package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;

public interface ISetParser {
    boolean canHandle(@NotNull String expression);
    @NotNull ISet parse(@NotNull String expression);
}