package pl.myc22ka.mathapp.model.function;

import pl.myc22ka.mathapp.model.function.functions.*;
import pl.myc22ka.mathapp.utils.math.MathUtils;

public class FunctionFactory {

    public static Function create(FunctionTypes type, String rawExpression) {
        return switch (type) {
            case ABSOLUTE -> new Absolute(rawExpression);
            case CONSTANT -> new Constant(rawExpression);
            case EXPONENTIAL -> new Exponential(rawExpression);
            case LINEAR -> new Linear(rawExpression);
            case LOGARITHMIC -> new Logarithmic(rawExpression); 
            case POLYNOMIAL -> new Polynomial(rawExpression);
            case QUADRATIC -> new Quadratic(rawExpression);
            case RATIONAL -> new Rational(rawExpression);    
            case SQUAREROOT -> new SquareRoot(rawExpression);  
            case TRIGONOMETRIC -> new Trigonometric(rawExpression);
            default -> new Function(type, MathUtils.detectFirstVariable(rawExpression), rawExpression);
        };
    }

    public static Function create(String rawExpression) {
        FunctionTypes type = FunctionTypeDetector.detect(rawExpression);
        return create(type, rawExpression);
    }
}
