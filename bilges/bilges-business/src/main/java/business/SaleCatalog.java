package business;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import facade.exceptions.ApplicationException;

/**
 * A catalog for sales
 * 
 * @author fmartins
 * @version 1.1 (17/04/2015)
 *
 */
@Stateless
public class SaleCatalog {

	/**
	 * Entity manager for accessing the persistence service 
	 */
	@PersistenceContext
	private EntityManager em;

	/**
	 * Finds a sale given its id.
	 * 
	 * @param id The id of the sale to fetch from the repository
	 * @return The Sale object corresponding to the sale with the id.
	 * @throws ApplicationException When the sale with the id is not found.
	*/
	public Sale getSaleById(int id) throws ApplicationException {
		Sale s = em.find(Sale.class, id);
		if (s == null)
			throw new ApplicationException("Sale with id " + id + " not found");
		else
			return s;
	}

	/**
	 * Creates a new sale and adds it to the repository
	 * 
	 * @param customer The customer the sales belongs to
	 * @return The newly created sale
	 */
	public Sale newSale (Customer customer) {
		Sale sale = new Sale(new Date(), customer);
		em.persist(sale);
		return sale;
	}

}
