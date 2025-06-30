package pl.myc22ka.mathapp.exceptions;

public class ServerError extends RuntimeException {
    public ServerError(ServerErrorMessages errorMessage) {
        super(errorMessage.toString());
    }

    public ServerError(String message) {
        super(message);
    }
}
