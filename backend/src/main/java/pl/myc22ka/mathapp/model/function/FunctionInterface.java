package pl.myc22ka.mathapp.model.function;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.utils.functions.ConditionRoots;
import pl.myc22ka.mathapp.utils.functions.Point;

import java.util.List;

public interface FunctionInterface {
    List<IExpr> getRealRoots();
    List<IExpr> getRealRoots(double min, double max);

    List<ConditionRoots> getRealConditionRoots(ISymbol symbol);

    List<IExpr> getAllRoots();

    IExpr getVaraibles();

    IExpr getDerivative();

    IExpr getRange();

    IExpr getDomain();

    IExpr getIntegral();

    IExpr getFactoredForm();

    IExpr getFunctionValue(IExpr x);

    IExpr getFunctionValue(String function, IExpr x);

    boolean isPointOnSlope(Point point);

    Function plus(Function other);

    Function minus(Function other);

    Function times(Function other);

    Function divide(Function other);

    Function composition(Function other);
}
