package com.julioromano.batchimporter.exceptions;

/**
 * {@code DirectoryCreationException} is an exception that represents a problem
 * to create a directory needed by the application.
 *
 * <p>{@code DirectoryCreationException} and its subclasses are <em>unchecked
 * exceptions</em>.  Unchecked exceptions do <em>not</em> need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public class DirectoryCreationException extends RuntimeException {

    public DirectoryCreationException(Throwable cause) {
        super(cause);
    }

    public DirectoryCreationException(String message) {
        super(message);
    }

}
