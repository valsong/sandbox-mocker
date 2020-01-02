package com.valsong.sandbox.mocker.exception;


/**
 * MockException
 *
 * @author Val Song
 */
public class MockException extends RuntimeException {

    private static final long serialVersionUID = 5583304585811599178L;

    /**
     * Constructs an MockException with no detail message.
     */
    public MockException() {
    }

    /**
     * Constructs an MockException with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public MockException(String message) {
        super(message);
    }


    /**
     * @param message
     * @param cause
     */
    public MockException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * @param cause
     */
    public MockException(Throwable cause) {
        super(cause);
    }
}
