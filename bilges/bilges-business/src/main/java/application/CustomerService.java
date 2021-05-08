package application;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import business.AddCustomerHandler;
import facade.dto.CustomerDTO;
import facade.dto.DiscountDTO;
import facade.exceptions.ApplicationException;
import facade.interfaces.IDiscount;
import facade.services.ICustomerServiceRemote;

/**
 * A service that supports the addition of customer. 
 * The purpose of this class is to provide access to the business layer to remote clients. 
 * It also hides the business layer so clients do  not interact directly with the business layer. 

 * 
 * @author fmartins
 * @version 1.1 (16/2/2017)
 */
@Stateless
public class CustomerService implements ICustomerServiceRemote {

	/**
	 * The business object façade that handles this use case request
	 */
	@EJB
	private AddCustomerHandler customerHandler;
	
	/**
	 * Adds a customer given its VAT number, denomination, phone number,
	 * and discount type.
	 * 
	 * @param vat The VAT number of the customer to add to the system
	 * @param denomination The customer's denomination 
	 * @param phoneNumber The customer's phone number
	 * @param discountCode The customer's discount code
	 * @throws ApplicationException In case the customer could not be added.
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public CustomerDTO addCustomer(int vat, String denomination, int phoneNumber, 
			int discountType) throws ApplicationException {
		return customerHandler.addCustomer(vat, denomination, phoneNumber, discountType);
	}
	
	
	/**
	 * Adds a customer given its VAT number, denomination, phone number,
	 * and discount type.
	 * 
	 * @param vat The VAT number of the customer to add to the system
	 * @param denomination The customer's denomination 
	 * @param phoneNumber The customer's phone number
	 * @param discountCode The customer's discount code
	 * @throws ApplicationException In case the customer could not be added.
	 */
	public Collection<DiscountDTO> getDiscounts() throws ApplicationException {
		List<DiscountDTO> result = new LinkedList<>(); 
		for (IDiscount discount : customerHandler.getDiscounts())
			result.add(new DiscountDTO(discount.getId(), discount.getDescription()));
		return result;
	}

}