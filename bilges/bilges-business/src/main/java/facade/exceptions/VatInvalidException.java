package facade.exceptions;

/**
 * The application exception that signals that the number given is
 * not a valid vat.
 * 
 * @author alopes
 *
 */
public class VatInvalidException extends ApplicationException {
		

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -5397173632387633192L;


	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public VatInvalidException(String vatNumber) {
		super (vatNumber);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public VatInvalidException(String vatNumber, Exception e) {
		super (vatNumber, e);
	}

}
