package pl.myc22ka.mathapp.exceptions;

import org.jetbrains.annotations.NotNull;

public class ServerError extends RuntimeException {
    public ServerError(@NotNull ServerErrorMessages errorMessage) {
        super(errorMessage.toString());
    }

    public ServerError(String message) {
        super(message);
    }
}
