package business;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import facade.exceptions.ApplicationException;

/**
 * A sale
 *	
 * @author fmartins
 * @author alopes
 * @version 1.2 (6/04/2021)
 * 
 */
@Entity 
public class Sale {

	/**
	 * Sale primary key. Needed by JPA. Notice that it is not part of the
	 * original domain model.
	 */
	@Id	@GeneratedValue private int id;

	/**
	 * The date the sale was made 
	 */
	@Temporal(TemporalType.DATE) private Date date;

	/**
	 * The time the sale is registered. 
	 * Illustrates the support of JPA 2.2 to classes of java.Time classes
	 */
	@SuppressWarnings("unused") 
	private LocalTime time;

	/**
	 * Whether the sale is open or closed. 
	 */
	@Enumerated(STRING) private SaleStatus status;

	@ManyToOne private Customer customer;

	/**
	 * The products of the sale
	 */
	@OneToMany(cascade = ALL) @JoinColumn
	private List<SaleProduct> saleProducts;

	/**
	 * The version for concurrency control
	 */
	@Version
	private long version;
	
	// 1. constructor

	/**
	 * Constructor needed by JPA.
	 */
	Sale () {
	}

	/**
	 * Creates a new sale given the date it occurred and the customer that
	 * made the purchase.
	 * 
	 * @param date The date that the sale occurred
	 * @param customer The customer that made the purchase
	 */
	public Sale(Date date, Customer customer) {
		this.date = date;
		this.time = LocalTime.now();
		this.customer = customer;
		this.status = SaleStatus.OPEN;
		this.saleProducts = new LinkedList<SaleProduct>();
	}


	// 2. getters and setters

	/**
	 * @return The sale's id 
	 */
	public int getNumber() {
		return id;
	}

	/**
	 * @return The customer of this sale.
	 */
	public Customer getCustomer() {
		return customer;
	}
	
	/**
	 * @return Whether the sale is open
	 */
	public boolean isOpen() {
		return status == SaleStatus.OPEN;
	}

	
	// 3. sale's logic

	/**
	 * @return The sale's total 
	 */
	public double total() {
		double total = 0;
		for (SaleProduct sp : saleProducts)
			total += sp.getSubTotal();
		return total;
	}

	/**
	 * @return The sale's amount eligible for discount
	 */
	public double eligibleDiscountTotal () {
		double total = 0;
		for (SaleProduct sp : saleProducts)
			total += sp.getEligibleSubtotal();
		return total;
	}

	/**
	 * @return Computes the sale's discount amount based on the discount type of the customer.
	 */
	public double discount () {
		Discount discount = customer.getDiscountType();
		return discount.computeDiscount(this);
	}


	/**
	 * Adds a product to the sale
	 * 
	 * @param product The product to sale
	 * @param qty The amount of the product being sold
	 */
	public void addProductToSale(Product product, double qty) throws ApplicationException {		
		saleProducts.add(new SaleProduct(product, qty));
	}

}
