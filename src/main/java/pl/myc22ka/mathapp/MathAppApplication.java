package pl.myc22ka.mathapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.Point;
import pl.myc22ka.mathapp.model.functions.Linear;
import pl.myc22ka.mathapp.model.functions.Polynomial;
import pl.myc22ka.mathapp.model.functions.Quadratic;

import java.util.List;

@SpringBootApplication
public class MathAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathAppApplication.class, args);


		Function function1 = new Linear('x');
		function1.generateFunctionFromAnswers(List.of(3.0));

//		System.out.println("Function: " + function1.getFunctionString());
//		System.out.println("Domain: " + function1.getDomain());
		System.out.println("Roots: " + function1.getRealRoots());
//		System.out.println("Range: " + function1.getRange());
//		System.out.println("Integral: " + function1.getIntegral());

		System.out.println("----------------------------------------------------");

		Quadratic[] functions = {
				new Quadratic('x'),                                 										// Random quadratic function
				new Quadratic(2, -3, 5, 'x'),                       			// Quadratic from coefficients
				new Quadratic(List.of(3.0, -2.0), 'x'),            										// Quadratic from roots
				new Quadratic(new Point(1, 4), 'x'),                									// Quadratic from vertex
				new Quadratic(new Point(1, 4), 2, 'x'),             						// Quadratic from vertex + coefficient
		};

//		for (int i = 0; i < functions.length; i++) {
//			System.out.println("Function " + (i + 1) + ": " + functions[i].getFunctionString());
//			System.out.println("Function Factored " + (i + 1) + ": " + functions[i].getCanonicalForm());
//			System.out.println("Function Canonical " + (i + 1) + ": " + functions[i].getFactoredForm());
//			System.out.println("Domain: " + functions[i].getDomain());
//			System.out.println("Roots: " + functions[i].getRoots());
//			System.out.println("Range: " + functions[i].getRange());
//			System.out.println("Integral: " + functions[i].getIntegral());
//			System.out.println("Is point on slope? Point(1, 4) " + functions[i].isPointOnSlope(new Point(1, 4)));
//			System.out.println("----------------------------------------------------");
//		}


		Function function3 = new Polynomial(List.of(1.0,1.0,1.0,1.0),'x');
//		function3.generateFunctionFromAnswers(List.of(1.0, 2.0, 3.0));

		System.out.println("Function: " + function3.getFunctionString());
//		System.out.println("Domain: " + function3.getDomain());
		System.out.println("Roots: " + function3.getRealRoots());
		System.out.println("All Roots: " + function3.getAllRoots());
//		System.out.println("Range: " + function3.getRange());
//		System.out.println("Integral: " + function3.getIntegral());

		System.out.println("----------------------------------------------------");

	}
}
