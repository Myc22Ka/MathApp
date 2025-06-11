package pl.myc22ka.mathapp.utils.math;

import lombok.experimental.UtilityClass;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

@UtilityClass
public class MathSymbols {

    private static final ISymbol REAL = F.Dummy("ℝ");
    private static final ISymbol NULL_SET = F.Dummy("∅");
    private static final IExpr IDENTITY = F.Dummy("IDENTITY");
    private static final IExpr CONTRADICTION = F.Dummy("CONTRADICTION");

    public IExpr belongs(ISymbol what, ISymbol to){
        return F.Element(what, to);
    }

    public ISymbol getReal(){
        return REAL;
    }

    public ISymbol getNullSet(){
        return NULL_SET;
    }

    public IExpr getIdentity(){
        return IDENTITY;
    }

    public IExpr getContradiction(){
        return CONTRADICTION;
    }
}