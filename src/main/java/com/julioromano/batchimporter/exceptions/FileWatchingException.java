package com.julioromano.batchimporter.exceptions;

/**
 * {@code FileWatchingException} is an exception that represents a problem
 * when watching new files to be parsed and analyzed.
 *
 * <p>{@code FileWatchingException} and its subclasses are <em>unchecked
 * exceptions</em>.  Unchecked exceptions do <em>not</em> need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public class FileWatchingException extends RuntimeException {

    public FileWatchingException(Throwable cause) {
        super(cause);
    }

    public FileWatchingException(String message) {
        super(message);
    }

}
