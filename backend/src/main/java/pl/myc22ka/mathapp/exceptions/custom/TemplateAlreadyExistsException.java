package pl.myc22ka.mathapp.exceptions.custom;

public class TemplateAlreadyExistsException extends RuntimeException {
    public TemplateAlreadyExistsException(String message) {
        super(message);
    }
}
