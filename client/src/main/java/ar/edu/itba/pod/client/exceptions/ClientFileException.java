package ar.edu.itba.pod.client.exceptions;

public class ClientFileException extends ClientException {
    public ClientFileException(String message) {
        super(message);
    }

    public ClientFileException(String message, Throwable cause) {
        super(message, cause);
    }
}