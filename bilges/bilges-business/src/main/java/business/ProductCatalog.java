package business;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import facade.exceptions.ApplicationException;

/**
 * A catalog of products
 * 
 * @author fmartins
 * @author alopes
 * @version 1.2 (15/02/2021)
 *
 */
@Stateless
public class ProductCatalog {

	/**
	 * Entity manager for accessing the persistence service 
	 */
	@PersistenceContext
	private EntityManager em;


	/**
	 * Finds a product given its code
	 * 
	 * @param prodCod The code of the product to find
	 * @return The product associated with the product code
	 * @throws ApplicationException When the product with a given prodCod is not found
	 */
	public Product getProduct (int prodCod) throws ApplicationException {
		TypedQuery<Product> query = em.createNamedQuery(Product.FIND_BY_PRODUCT_CODE, Product.class);
		try {
			query.setParameter(Product.PRODUCT_CODE, prodCod);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new ApplicationException ("Product with code " + prodCod + " does not exist", e);
		}
	}	
}
