package business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * A customer
 *	
 * @author fmartins
 * @author alopes
 * @version 1.2 (12/03/2021)
 * 
 */
@Entity  
@NamedQueries({
	@NamedQuery(name=Customer.FIND_BY_VAT_NUMBER, query="SELECT c FROM Customer c WHERE c.vatNumber = :" + 
		Customer.NUMBER_VAT_NUMBER),
	@NamedQuery(name=Customer.EXISTS_BY_VAT_NUMBER, query="SELECT COUNT(c) FROM Customer c WHERE c.vatNumber = :" 
			+   Customer.NUMBER_VAT_NUMBER)
})
public class Customer {

	// Named query name constants
	public static final String FIND_BY_VAT_NUMBER = "Customer.findByVATNumber";
	public static final String EXISTS_BY_VAT_NUMBER = "Customer.existsByVATNumber";
	public static final String NUMBER_VAT_NUMBER = "vatNumber";

	
	// Customer attributes 

	/**
	 * Customer primary key. Needed by JPA. Notice that it is not part of the
	 * original domain model.
	 */
	@Id @GeneratedValue private int id;
	
	/**
	 * Customer's VAT number
	 */
	@Column(nullable = false, unique = true) private int vatNumber;
	
	/**
	 * Customer's name. In case of a company, the represents its commercial denomination 
	 */
	@Column(nullable = false) private String designation;
	
	/**
	 * Customer's contact number
	 */
	@SuppressWarnings("unused")
	private int phoneNumber;

	/**
	 * Customer's discount.
	 */
	@ManyToOne(optional = false) private Discount discount;
		
	// 1. constructor 

	/**
	 * Constructor needed by JPA.
	 */
	Customer() {
	}
	
	/**
	 * Creates a new customer given its VAT number, its designation, phone contact, and discount type.
	 * 
	 * @param vatNumber The customer's VAT number
	 * @param designation The customer's designation
	 * @param phoneNumber The customer's phone number
	 * @param discountType The customer's discount type
	 * @requires isValidVAT(vat) 
	 */
	public Customer(int vatNumber, String designation, int phoneNumber, Discount discountType) {
		this.vatNumber = vatNumber;
		this.designation = designation;
		this.phoneNumber = phoneNumber;
		this.discount = discountType;
	}

	
	// 2. getters and setters
	
	/**
	 * @return The discount type of the customer
	 */
	public Discount getDiscountType() {
		return discount;
	}

	/**
	 * For testing purposes only.
	 * 
	 * @return The customer number
	 */
	public int getVATNumber() {
		return vatNumber;
	}
	
	/**
	 * @return The description of the customer
	 */
	public String getDesignation() {
		return designation;
	}
	
	/**
	 * @return The customer's id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks if a VAT number is valid.
	 * 
	 * @param vat The number to be checked.
	 * @return Whether the VAT number is valid. 
	 */
	public static boolean isValidVAT(int vat) {
		// If the number of digits is not 9, error!
		if (vat < 100000000 || vat > 999999999)
			return false;
		
		// If the first number is not 1, 2, 3, 5, 6 error!
		int firstDigit = vat / 100000000;
		if (firstDigit > 3 && firstDigit < 8 &&
			firstDigit != 5 && firstDigit != 6)
			return false;
		
		return vat % 10 == mod11(vat);
	}


	/**
	 * @param num The number to compute the modulus 11.
	 * @return The modulus 11 of num.
	 */
	private static int mod11(int num) {
		// Checks the congruence modules 11.
		int sum = 0;
		int quocient = num / 10;
		
		for (int i = 2; i < 10 && quocient != 0; i++) {
			sum += quocient % 10 * i;
			quocient /= 10;
		}
		
		int checkDigitCalc = 11 - sum % 11;
		return checkDigitCalc == 10 ? 0 : checkDigitCalc;
	}

}