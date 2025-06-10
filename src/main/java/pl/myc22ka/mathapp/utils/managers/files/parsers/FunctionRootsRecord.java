package pl.myc22ka.mathapp.utils.managers.files.parsers;

import org.matheclipse.core.interfaces.IExpr;

import java.util.List;

public record FunctionRootsRecord(
        String key,
        List<IExpr> values
) {
}