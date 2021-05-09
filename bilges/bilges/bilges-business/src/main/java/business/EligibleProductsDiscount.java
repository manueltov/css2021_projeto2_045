package business;

import javax.persistence.Entity;

/**
 * A discount based on the products marked as eligible for discount
 * 
 * @author fmartins
 * @version 1.1 (17/04/2015)
 *
 */
@Entity
public class EligibleProductsDiscount extends Discount {
	
	// Just for illustrative purposes of SaleSys with Customers creation

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -6895059393437557215L;

	/**
	 * Constructor needed by JPA 
	 */
	EligibleProductsDiscount() {
	}
	
	/**
	 * Creates a discount that will apply a percentage over the total amount
	 * of products marked as eligible for discount.
	 * 
	 * @param discountId The id of the discount
	 * @param description The discount description
	 * @param percentage The percentage to be applied
	 */
	public EligibleProductsDiscount(int discountId, String description, double percentage) {
		super (discountId, description);
	}

	@Override
	public double computeDiscount(Sale sale) {
		// Just for illustrative purposes of SaleSys with Customers creation
		return 0;
	}
	
}
