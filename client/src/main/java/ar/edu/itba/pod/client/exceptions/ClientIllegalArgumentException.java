package ar.edu.itba.pod.client.exceptions;

public class ClientIllegalArgumentException extends ClientException {
    public ClientIllegalArgumentException(String message) {
        super(message);
    }

    public ClientIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
