package pl.myc22ka.mathapp.model;

import org.matheclipse.core.interfaces.IExpr;

import java.util.List;

public interface FunctionInterface {

    void generateRandomFunction();

    String getFunctionString();

    void generateFunctionFromAnswers(List<IExpr> answers);
}
