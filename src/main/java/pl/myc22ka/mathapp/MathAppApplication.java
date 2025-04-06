package pl.myc22ka.mathapp;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.myc22ka.mathapp.model.functions.Function;
import pl.myc22ka.mathapp.model.functions.Linear;
import pl.myc22ka.mathapp.model.functions.Trigonometric;
import pl.myc22ka.mathapp.utils.MathUtils;

@SpringBootApplication
public class MathAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathAppApplication.class, args);

//		Function function1 = new Linear(F.x);
//		function1.generateFunctionFromAnswers(List.of(S.Rational.of(1, 2)));
//
//		System.out.println("Function: " + function1);
//		System.out.println("Domain: " + function1.getDomain());
//		System.out.println("RealRoots: " + function1.getRealRoots());
//		System.out.println("Range: " + function1.getRange());
//		System.out.println("Integral: " + function1.getIntegral());
//		System.out.println("Derivative: " + function1.getDerivative());
//		System.out.println("Factored form: " + function1.getFactoredForm());
//
//		System.out.println("----------------------------------------------------");
//
//		Quadratic[] functions = {
//				new Quadratic(F.x), // Random quadratic function
//				new Quadratic(F.ZZ(2), F.ZZ(-3), F.ZZ(5), F.x), // Quadratic from coefficients
//				new Quadratic(List.of(F.ZZ(3), F.ZZ(-2)), F.x), // Quadratic from roots
//				// new Quadratic(new Point(F.ZZ(1), F.ZZ(4)), F.x), // Quadratic from vertex
//				new Quadratic(new Point(F.ZZ(1), F.ZZ(4)), F.ZZ(2), F.x), // Quadratic from vertex + coefficient
//		};
//
//		for (int i = 0; i < functions.length; i++) {
//			System.out.println("Function " + (i + 1) + ": " +
//					functions[i]);
//			System.out.println("Function Factored " + (i + 1) + ": " +
//					functions[i].getFactoredForm());
//			System.out.println("Domain: " + functions[i].getDomain());
//			System.out.println("Roots: " + functions[i].getRealRoots());
//			System.out.println("Range: " + functions[i].getRange());
//			System.out.println("Integral: " + functions[i].getIntegral());
//			System.out.println("Is point on slope? Point(1, 4) " +
//					functions[i].isPointOnSlope(new Point(F.ZZ(1), F.ZZ(4))));
//			System.out.println("----------------------------------------------------");
//		}
//
//		Polynomial function3 = new Polynomial(List.of(F.ZZ(4), F.ZZ(2), F.ZZ(2), F.C0), F.x);
//		// function3.generateFunctionFromAnswers(List.of(1.0, 2.0, 3.0));
//
//		System.out.println("Function: " + function3);
//		System.out.println("Domain: " + function3.getDomain());
//		System.out.println("Roots: " + function3.getRealRoots());
//		System.out.println("All Roots: " + function3.getAllRoots());
//		System.out.println("Canonical Form: " + function3.getFactoredForm());
//		System.out.println("Range: " + function3.getRange());
//		System.out.println("Integral: " + function3.getIntegral());
//
//		System.out.println("----------------------------------------------------");
//
//		FunctionInterface proxiedFunction = NotFullyImplementedProxy.createProxy(
//				new Linear(F.x),
//				FunctionInterface.class
//		);
//		System.out.println("Function: " + proxiedFunction);
//
//		// Calling implemented methods works normally
//		System.out.println("Roots: " + proxiedFunction.getRealRoots());
//
//		System.out.println("----------------------------------------------------");

		var linear1 = new Function("x+1", F.x);
		var linear2 = new Function("x+3", F.x);

		var linear3 = linear1.plus(linear2);

		System.out.println(linear3);

//		var function = new Function("Cos(x)+Sin(3*x)", F.x);
//
//		var res2 = MathUtils.getAllRoots(function.getRealConditionRoots(F.k));
//
//		System.out.println(res2);
	}
}
