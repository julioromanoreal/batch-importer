package com.julioromano.batchimporter.exceptions;

/**
 * {@code ProcessOutputException} is an exception that represents
 * a problem when saving the output report into the specified directory.
 *
 * <p>{@code ProcessOutputException} and its subclasses are <em>unchecked
 * exceptions</em>.  Unchecked exceptions do <em>not</em> need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public class ProcessOutputException extends RuntimeException {

    public ProcessOutputException(Throwable cause) {
        super(cause);
    }

    public ProcessOutputException(String message) {
        super(message);
    }

}
