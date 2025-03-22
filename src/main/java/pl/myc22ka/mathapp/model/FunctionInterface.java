package pl.myc22ka.mathapp.model;

import java.util.List;

public interface FunctionInterface {
    void generateRandomFunction();
    String getFunctionString();
    void generateFunctionFromAnswers(List<Double> answers);
}
