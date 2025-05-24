package pl.myc22ka.mathapp.exceptions;

public class FunctionException extends RuntimeException {
    public FunctionException(FunctionErrorMessages errorMessage) {
        super(errorMessage.toString());
    }

    public FunctionException(String message) {
        super(message);
    }
}
