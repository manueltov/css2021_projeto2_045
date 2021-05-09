package presentation.web.model;

import java.util.LinkedList;

import facade.dto.DiscountDTO;
import facade.exceptions.ApplicationException;
import facade.services.ICustomerServiceRemote;


/**
 * Helper class to assist in the response of novo cliente.
 * This class is the response information expert.
 * 
 * @author fmartins
 *
 */
public class NewCustomerModel extends Model {

	private String designation;
	private String vatNumber;
	private String phoneNumber;
	private String discountType;

	//for illustration purposes of dealing with date 
	private String birthDate;
	
	private ICustomerServiceRemote customerService;
		
	public void setCustomerService(ICustomerServiceRemote customerService) {
		this.customerService = customerService;
	}
	

	public void setDesignation(String designation) {
		this.designation = designation;	
	}
	
	public String getDesignation() {
		return designation;
	}
	
	public String getVATNumber() {
		return vatNumber;
	}
	
	public void setVATNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountType() {
		return discountType;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthDate() {
		return birthDate;
	}
	
	public void clearFields() {
		designation = vatNumber = phoneNumber = "";
		discountType = "1";
	}
	
	public Iterable<DiscountDTO> getDiscounts () {
		try {
			return customerService.getDiscounts();
		} catch (ApplicationException e) {
			return new LinkedList<> ();		
		}
	}

	
}
