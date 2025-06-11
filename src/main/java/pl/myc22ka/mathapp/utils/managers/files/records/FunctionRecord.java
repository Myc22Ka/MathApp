package pl.myc22ka.mathapp.utils.managers.files.records;

import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionTypes;
import pl.myc22ka.mathapp.model.function.Function;

import java.util.List;

public record FunctionRecord (
        Function function,
        FunctionTypes type,
        List<IExpr> roots,
        IExpr derivative,
        String range,
        String domain,
        IExpr integral
) {
}
