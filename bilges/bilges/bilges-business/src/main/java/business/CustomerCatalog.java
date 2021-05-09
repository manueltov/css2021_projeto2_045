package business;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import facade.exceptions.ApplicationException;

/**
 * A catalog of customers
 * 
 * @author fmartins
 * @author alopes
 * @version 1.1 (15/02/2021)
 *
 */
@Stateless
public class CustomerCatalog {

	/**
	 * Entity manager for accessing the persistence service 
	 */
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Finds a customer given its VAT number.
	 * 
	 * @param vat The VAT number of the customer to fetch from the repository
	 * @return The Customer object corresponding to the customer with the vat number.
	 * @throws ApplicationException When the customer with the vat number is not found.
	 */
	public Customer getCustomer (int vat) throws ApplicationException {
		TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_BY_VAT_NUMBER, Customer.class);
		query.setParameter(Customer.NUMBER_VAT_NUMBER, vat);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException ("Customer with VAT " + vat + " not found.");
		}
	}
	
	/**
	 * Finds a customer given its id.
	 * 
	 * @param id The id of the customer to fetch from the repository
	 * @return The Customer object corresponding to the customer with the id.
	 * @throws ApplicationException When the customer with the vat number is not found.
	*/
	public Customer getCustomerById(int id) throws ApplicationException {
		Customer c = em.find(Customer.class, id);
		if (c == null)
			throw new ApplicationException("Customer with id " + id + " not found");
		else
			return c;
	}
	
	/**
	 * Checks if a customer with a given VAT number already exists.
	 * 
	 * @param vat The VAT number of the customer to look for in the repository
	 * @return Whether the customer exists
	 */
	public boolean existsCustomer (int vat) {
		TypedQuery<Long> query = em.createNamedQuery(Customer.EXISTS_BY_VAT_NUMBER, Long.class);
		query.setParameter(Customer.NUMBER_VAT_NUMBER, vat);
		return query.getSingleResult() > 0;
	}
	
	
	
	/**
	 * Adds a new customer
	 * 
	 * @param vat The customer's VAT number
	 * @param designation The customer's designation
	 * @param phoneNumber The customer's phone number
	 * @param discountType The customer's discount type
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public Customer addCustomer (int vat, String designation, int phoneNumber, Discount discountType) {
		Customer customer = new Customer(vat, designation, phoneNumber, discountType);
		em.persist(customer);
		return customer;
	}
	

	public void deleteCustomer(int id) throws ApplicationException {
		Customer customer = em.find(Customer.class, id);
		if (customer == null)
			throw new ApplicationException("Customer with id " + id + " not found");
		em.remove(customer);
	}

}
