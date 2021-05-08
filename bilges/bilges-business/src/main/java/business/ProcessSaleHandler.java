package business;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import facade.dto.SaleDTO;
import facade.exceptions.ApplicationException;

/**
 * Handles use case process sale (only two operations are implemented: 
 * newSale followed by an arbitrary number of addProductToSale) 
 * 
 * @author fmartins
 * @author alopes
 * @version 1.1 (17/03/2021)
 */
@Stateless
public class ProcessSaleHandler {

	/**
	 * The customer's catalog
	 */
	@EJB
	private CustomerCatalog customerCatalog;

	/**
	 * The sale's catalog 
	 */
	@EJB
	private SaleCatalog saleCatalog;

	/**
	 * The product's catalog 
	 */
	@EJB
	private ProductCatalog productCatalog;


	/**
	 * Creates a new sale
	 * 
	 * @param vatNumber The customer's VAT number for the sale
	 * @return the number of the sale in the system
	 * @throws ApplicationException In case the vat does not identify a customer
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public int newSale (int vatNumber) throws ApplicationException {
		try {
			Customer customer = customerCatalog.getCustomer(vatNumber);
			Sale sale = saleCatalog.newSale(customer);
			return sale.getNumber();
		} catch (Exception e) {
			throw new ApplicationException("Error creating sale. " + e.getMessage());
		} 
	}

	/**
	 * Adds a product to the current sale. 
	 * 
	 * Note that this  method can raise EJB exceptions on exit, namely 
	 * javax.ejb.EJBTransactionRolledbackException if the transaction cannot commit.
	 * These exceptions need to be handled by the caller (dealing with this at the same
	 * class does not work).
	 * 
	 * @param number The number of the sale 
	 * @param prodCod The product code to be added to the sale 
	 * @param qty The quantity of the product sold
	 * @throws ApplicationException When the sale does not exist or is closed, the product code
	 * is not part of the product's catalog, or when there is not enough stock to fulfill the request
	 */	
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void addProductToSale(int number, int prodCod, double qty) throws ApplicationException {
		if(qty <= 0) {
			throw new ApplicationException("Error adding product to sale. Quantity must be positive.");	
		}
		try {	
			// Although sale is a good candidate to have the responsibility to
			// check the required conditions for adding a product to sale 
			// (see previous versions of SaleSys) herein we illustrate the situation
			// in which this responsibility is given to the handler 
			
			Sale sale = saleCatalog.getSaleById(number);
			if (!sale.isOpen())
				throw new ApplicationException("Cannot add products to a closed sale.");

			Product product = productCatalog.getProduct(prodCod);	
			if (product.getQty() < qty) 
				throw new ApplicationException("The stock of product " + product.getProdCod() + 
						"  is insuficient for the current sale ("  + product.getQty() + ")");

			sale.addProductToSale(product, qty);
			product.decrementQty(qty);
			
			// JPA 2.1 specification defines optimistic locking as:
			// The version attribute is updated by the persistence provider runtime 
			// when the object is written to the database. 
			// All non-relationship fields and proper ties and all relationships 
			// owned by the entity are included in version checks.
			// This includes owned relationships maintained in join tables.

			// This means that in the presence of two or more concurrent executions of this operation
			// over the same sale, only the first to ask for commit will succeed.
			// The others will find a stale state and will not be able to commit.
			
			// The same applies if we have  two or more concurrent executions of this operation
			// over the same product.
			
		} catch (Exception e) {
			throw new ApplicationException("Error adding product to sale. " + e.getMessage());
		} 
	}

	/**
	 * Get the sale details as a SaleDTO
	 * 
	 * @param number The number of the sale 
	 * @return saleDTO with sale details
	 * @throws ApplicationException When the sale does not exist
	 */	
	public SaleDTO getSaleDetails(int number) throws ApplicationException {
		Sale sale = saleCatalog.getSaleById(number);
		return new SaleDTO(number,sale.getCustomer().getDesignation(), sale.total());
	}


}
