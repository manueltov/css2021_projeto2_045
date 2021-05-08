package presentation.web.model;

/**
 * Helper class to assist in the response of add entry to sale.
 * 
 * @author alopes
 *
 */
public class AddProductToSaleModel extends Model {

	private String sale;
	private String customer;
	private String total;
	
	//form fields
	private String product;
	private String quantity;
	
	public AddProductToSaleModel() {
		sale = "";
		customer = "";
		total = "";
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	public void clearFormFields() {
		product = "";
		quantity = "";
	}


}
