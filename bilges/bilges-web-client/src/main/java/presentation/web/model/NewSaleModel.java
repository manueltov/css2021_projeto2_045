package presentation.web.model;

/**
 * Model class to assist in the response of a new customer action.
 * This class is the response information expert.
 * 
 * @author fmartins
 * @author alopes
 *
 */
public class NewSaleModel extends Model {

	private String vatNumber;
		
	public String getVATNumber() {
		return vatNumber;
	}
	
	public void setVATNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
		
	public void clearFields() {
		vatNumber = "";
	}
	
}
