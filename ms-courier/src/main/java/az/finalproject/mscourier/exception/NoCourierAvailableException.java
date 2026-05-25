package az.finalproject.mscourier.exception;

public class NoCourierAvailableException extends RuntimeException {
    public NoCourierAvailableException(String message) {
        super(message);
    }
}
