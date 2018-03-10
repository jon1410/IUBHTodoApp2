package de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions;

/**
 * Created by ivanj on 10.03.2018.
 */

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }
}
