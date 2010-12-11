/**
 * 
 */
package qc.core.exception;

/**
 * 异常的简单封装
 * 
 * @author dragon
 * 
 */
public class QcException extends RuntimeException {
	private static final long serialVersionUID = -900402016781472L;

	public QcException() {
		super();
	}

	public QcException(String message, Throwable cause) {
		super(message, cause);
	}

	public QcException(String message) {
		super(message);
	}

	public QcException(Throwable cause) {
		super(cause);
	}
}
