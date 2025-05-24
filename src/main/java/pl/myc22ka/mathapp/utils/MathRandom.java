package pl.myc22ka.mathapp.utils;

import java.util.Random;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

public class MathRandom {

    private static final Random random = new Random();

    public static IExpr generateRandomReal() {
        double val = random.nextDouble() * 200 - 100; // random between -100 and 100
        return F.num(val);
    }

    public static IExpr generateRandomComplex() {
        double real = random.nextDouble() * 200 - 100;
        double imag = random.nextDouble() * 200 - 100;

        IExpr realPart = F.num(real);
        IExpr imagPart = F.num(imag);
        IExpr imagUnit = F.symbol("I");

        return F.Plus(realPart, F.Times(imagPart, imagUnit));
    }

}
