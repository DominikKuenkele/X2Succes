package util.exception;

/**
 * throwable Validation-Exception for logical-layer
 * 
 * @author domin
 *
 */
public class ValidateArgsException extends Exception {

	private static final long serialVersionUID = 4732122161608535198L;

	/**
	 * 
	 */
	public ValidateArgsException() {
		super();
	}

	/**
	 * @param message
	 */
	public ValidateArgsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ValidateArgsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ValidateArgsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ValidateArgsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
