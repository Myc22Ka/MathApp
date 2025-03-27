package pl.myc22ka.mathapp.model;

import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

public interface FunctionInterface {
    List<IExpr> getRealRoots();

    List<IExpr> getAllRoots();

    IExpr getDerivative();

    IExpr getRange();

    IExpr getDomain();

    IExpr getIntegral();

    IExpr getFactoredForm();

    IExpr getFunctionValue(IExpr x);

    IExpr getFunctionValue(String function, IExpr x);

    boolean isPointOnSlope(Point point);
}
