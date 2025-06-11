package pl.myc22ka.mathapp;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.model.function.functions.Exponential;
import pl.myc22ka.mathapp.model.function.functions.Logarithmic;
import pl.myc22ka.mathapp.model.function.functions.Rational;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class MathAppApplication {

	private static final ExprEvaluator evaluator = new ExprEvaluator();

	public static void main(String[] args) {

		// Disable logging for JAS packages
		Logger jasLogger = Logger.getLogger("edu.jas");
		jasLogger.setLevel(Level.OFF);

		SpringApplication.run(MathAppApplication.class, args);

		// Function function1 = new Linear(F.x);
		// function1.generateFunctionFromAnswers(List.of(S.Rational.of(1, 2)));
		//
		// System.out.println("Function: " + function1);
		// System.out.println("Domain: " + function1.getDomain());
		// System.out.println("RealRoots: " + function1.getRealRoots());
		// System.out.println("Range: " + function1.getRange());
		// System.out.println("Integral: " + function1.getIntegral());
		// System.out.println("Derivative: " + function1.getDerivative());
		// System.out.println("Factored form: " + function1.getFactoredForm());
		//
		// System.out.println("----------------------------------------------------");
		//
		// Quadratic[] functions = {
		// new Quadratic(F.x), // Random quadratic function
		// new Quadratic(F.ZZ(2), F.ZZ(-3), F.ZZ(5), F.x), // Quadratic from
		// coefficients
		// new Quadratic(List.of(F.ZZ(3), F.ZZ(-2)), F.x), // Quadratic from roots
		// // new Quadratic(new Point(F.ZZ(1), F.ZZ(4)), F.x), // Quadratic from vertex
		// new Quadratic(new Point(F.ZZ(1), F.ZZ(4)), F.ZZ(2), F.x), // Quadratic from
		// vertex + coefficient
		// };
		//
		// for (int i = 0; i < functions.length; i++) {
		// System.out.println("Function " + (i + 1) + ": " +
		// functions[i]);
		// System.out.println("Function Factored " + (i + 1) + ": " +
		// functions[i].getFactoredForm());
		// System.out.println("Domain: " + functions[i].getDomain());
		// System.out.println("Roots: " + functions[i].getRealRoots());
		// System.out.println("Range: " + functions[i].getRange());
		// System.out.println("Integral: " + functions[i].getIntegral());
		// System.out.println("Is point on slope? Point(1, 4) " +
		// functions[i].isPointOnSlope(new Point(F.ZZ(1), F.ZZ(4))));
		// System.out.println("----------------------------------------------------");
		// }
		//
		// Polynomial function3 = new Polynomial(List.of(F.ZZ(4), F.ZZ(2), F.ZZ(2),
		// F.C0), F.x);
		// // function3.generateFunctionFromAnswers(List.of(1.0, 2.0, 3.0));
		//
		// System.out.println("Function: " + function3);
		// System.out.println("Domain: " + function3.getDomain());
		// System.out.println("Roots: " + function3.getRealRoots());
		// System.out.println("All Roots: " + function3.getAllRoots());
		// System.out.println("Canonical Form: " + function3.getFactoredForm());
		// System.out.println("Range: " + function3.getRange());
		// System.out.println("Integral: " + function3.getIntegral());
		//
		// System.out.println("----------------------------------------------------");
		//
		// FunctionInterface proxiedFunction = NotFullyImplementedProxy.createProxy(
		// new Linear(F.x),
		// FunctionInterface.class
		// );
		// System.out.println("Function: " + proxiedFunction);
		//
		// // Calling implemented methods works normally
		// System.out.println("Roots: " + proxiedFunction.getRealRoots());
		//
		// System.out.println("----------------------------------------------------");

		// var linear1 = new Function("x+2", F.x);
		// var linear2 = new Function("(x+3)(x+1)", F.x);

		// var linear3 = linear1.times(linear2);

		// System.out.println(linear3);

//		var func1 = new Function("x^2", F.x);
//		var func2 = new Function("x+2", F.x);
//
//		var func3 = func1.times(func2);
//		var func4 = func1.minus(func2);
//		var func5 = func1.plus(func2);
//		var func6 = func1.divide(func2);
//		var func7 = func1.composition(func2);
//
//		System.out.println("* Func3: " + func3);
//		System.out.println("- Func4: " + func4);
//		System.out.println("+ Func5: " + func5);
//		System.out.println("/ Func6: " + func6);
//		System.out.println("c Func7: " + func7);

		// var function = new Function("Sin(x)+Cos(3*x)", F.x);

		// var res2 = MathUtils.getAllRoots(function.getRealConditionRoots(F.k));

		// System.out.println(res2);

		var function = new Function("e^x");

		System.out.println("Function: " + function);
		System.out.println("Domain: " + function.getDomain());
		System.out.println("Roots: " + function.getRealRoots());
		System.out.println("All Roots: " + function.getAllRoots());
		System.out.println("Canonical Form: " + function.getFactoredForm());
		System.out.println("Range: " + function.getRange());
		System.out.println("Integral: " + function.getIntegral());

		System.out.println("----------------------------------------------------");

//		var linear = new Linear(F.ZZ(1), F.ZZ(2), F.x);
//
//		var function = new SquareRoot(linear, 2);
//
//		 System.out.println("Function: " + function);
//		 System.out.println("Domain: " + function.getDomain());
//		 System.out.println("Roots: " + function.getRealRoots());
//		 System.out.println("All Roots: " + function.getAllRoots());
//		 System.out.println("Canonical Form: " + function.getFactoredForm());
//		 System.out.println("Range: " + function.getRange());
//		 System.out.println("Integral: " + function.getIntegral());

		System.out.println("----------------------------------------------------");

		var e = new Function(F.e);
		var coef = F.ZZ(1);
		var exp = new Function("x");

		var expFunction = new Exponential(e, coef, exp);

		System.out.println("Exponential Function: " + expFunction);
		System.out.println("Domain: " + expFunction.getDomain());
		System.out.println("Roots: " + expFunction.getRealRoots());
		System.out.println("All Roots: " + expFunction.getAllRoots());
		System.out.println("Range: " + expFunction.getRange());
		System.out.println("Integral: " + expFunction.getIntegral());

		System.out.println("----------------------------------------------------");

		var logFunction = new Logarithmic(F.ZZ(2), F.ZZ(1));

		System.out.println("Logarithmic Function: " + logFunction);
		System.out.println("Domain: " + logFunction.getDomain());
		System.out.println("Roots: " + logFunction.getRealRoots());
		System.out.println("All Roots: " + logFunction.getAllRoots());
		System.out.println("Range: " + logFunction.getRange());
		System.out.println("Integral: " + logFunction.getIntegral());

		System.out.println("----------------------------------------------------");

		var numerator = F.Plus(F.x, F.ZZ(2));
		var denominator = F.Plus(F.x, F.ZZ(-1));
		var rationalFunction = new Rational(numerator, denominator);

		System.out.println("Rational Function: " + rationalFunction);
		System.out.println("Domain: " + rationalFunction.getDomain());
		System.out.println("Roots: " + rationalFunction.getRealRoots());
		System.out.println("All Roots: " + rationalFunction.getAllRoots());
		System.out.println("Range: " + rationalFunction.getRange());
		System.out.println("Integral: " + rationalFunction.getIntegral());
	}
}
