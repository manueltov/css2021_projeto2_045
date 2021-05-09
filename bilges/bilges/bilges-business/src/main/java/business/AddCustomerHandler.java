package business;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import facade.dto.CustomerDTO;
import facade.exceptions.ApplicationException;
import facade.exceptions.CustomerWithVatAlreadydExistsException;
import facade.exceptions.VatInvalidException;
import facade.interfaces.IDiscount;

/**
 * Handles the add customer use case. This represents a very 
 * simplified use case with just one operation: addCustomer.
 * 
 * @author fmartins
 * @version 1.1 (17/04/2015)
 *
 */
@Stateless
public class AddCustomerHandler {

	/**
	 * The customer's catalog
	 */
	@EJB
	private CustomerCatalog customerCatalog;

	/**
	 * The discount's catalog 
	 */
	@EJB
	private DiscountCatalog discountCatalog;

	/**
	 * Adds a new customer with a valid Number. It checks that there is no other 
	 * customer in the database with the same VAT.
	 * 
	 * @param vat The VAT of the customer
	 * @param denomination The customer's name
	 * @param phoneNumber The customer's phone 
	 * @param discountType The type of discount applicable to the customer
	 * @throws ApplicationException When the VAT number is invalid (we check it according 
	 * to the Portuguese legislation) or already exists.
	 */
	public CustomerDTO addCustomer (int vat, String denomination, 
			int phoneNumber, int discountType) throws ApplicationException {
		if(!Customer.isValidVAT(vat)) {
			throw new VatInvalidException(Integer.toString(vat));	
		}
		if(customerCatalog.existsCustomer(vat)) {
			throw new CustomerWithVatAlreadydExistsException(Integer.toString(vat));	
		}
		try {
			Discount discount = discountCatalog.getDiscount(discountType);
			Customer c = customerCatalog.addCustomer(vat, denomination, phoneNumber, discount);
			return new CustomerDTO(c.getVATNumber(), c.getDesignation(), c.getId());
		} catch (Exception e) {
			throw new ApplicationException ("Error adding customer. " + e.getMessage());
		}
	}	

	/**
	 * Gets the discount list.
	 * 
	 * @throws ApplicationException When there is an error in getting this list.
	 */
	public List<IDiscount> getDiscounts() throws ApplicationException {
		return new LinkedList<>(discountCatalog.getDiscounts());
	}
}
