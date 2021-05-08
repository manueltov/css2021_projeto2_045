package facade.exceptions;


/**
 * The top level application exception.
 * In this simple example we use few type of exceptions.
 * In a more involving example, there should be subclasses of this class.
 * Note that low-level exceptions (like SQLExceptions) are wrapped in this
 * exception. 
 * 
 * @author fmartins
 *
 */
public class ApplicationException extends Exception {
	
	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public ApplicationException(String message) {
		super (message);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * Do not use for lower exception types that might not be 
	 * available in the receiver context.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public ApplicationException(String message, Exception e) {
		super (message, e);
	}

}
