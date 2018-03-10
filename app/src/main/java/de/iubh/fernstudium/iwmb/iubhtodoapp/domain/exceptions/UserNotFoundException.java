package de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions;

/**
 * Created by ivanj on 10.03.2018.
 */

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

}
