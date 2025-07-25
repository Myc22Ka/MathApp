package pl.myc22ka.mathapp.utils.managers.files.records;

import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.model.set.ISet;

import java.util.List;

public record FunctionRecord (
        Function function,
        FunctionType type,
        List<IExpr> roots,
        IExpr derivative,
        String range,
        String domain,
        IExpr integral
) {
}
