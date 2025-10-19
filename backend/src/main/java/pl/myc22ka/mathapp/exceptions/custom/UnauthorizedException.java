package pl.myc22ka.mathapp.exceptions.custom;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String msg){
        super("Unauthorized: " + msg);
    }
}

