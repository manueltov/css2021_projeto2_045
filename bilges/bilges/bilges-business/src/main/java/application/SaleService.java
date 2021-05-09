package application;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import business.ProcessSaleHandler;
import facade.dto.SaleDTO;
import facade.exceptions.ApplicationException;
import facade.services.ISaleServiceRemote;

/**
 * A service the supports several operations over sales: addition of a new sale, 
 * addition of an entry to an existing sale, close an existing sale. 
 *  
 * The purpose of this class is to provide access to the business layer to remote clients. 
 * It also hides the business layer so clients do  not interact directly with the business layer. 
 * 
 * @author alopes
 * @version 1.0 (6/4/2021)
 */
@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class SaleService implements ISaleServiceRemote {

	/**
	 * The business object façade that handles this use case request
	 */
	@EJB
	private ProcessSaleHandler saleHandler;

	/**
	 * Creates a new sale
	 * 
	 * @param vat The customer's VAT number for the sale
	 * @return 
	 * @throws ApplicationException In case no customer exists with given vat
	 */
	@Override
	public SaleDTO newSale(int vat) throws ApplicationException {
		int num = saleHandler.newSale(vat);
		return saleHandler.getSaleDetails(num);
	}

	/**
	 * Adds a product to the sale
	 * 
	 * @param number The sale number 
	 * @param prodCod The product code to be added to the sale 
	 * @param qty The quantity of the product sold
	 * @throws ApplicationException When the sale is closed, the product code
	 * is not part of the product's catalog, or when there is not enough stock
	 * to proceed with the sale
	 */
	@Override
	public SaleDTO addProductToSale(int num, int prodCod, double qty) throws ApplicationException {
		try {
			saleHandler.addProductToSale(num, prodCod, qty);
			return saleHandler.getSaleDetails(num);
		}catch(ApplicationException e) {
			throw e;
		}catch(EJBTransactionRolledbackException e) {
			throw new ApplicationException("Error adding product to sale. Concurrency issues occured. ");
		}catch(Exception e) {
			throw new ApplicationException("Error adding product to sale. Unknown problem.");
		}

	}

	//TODO complete with lacking operations on sales 

	/**
	 * Gets details of a sale
	 * 
	 * @param num The number of the sale
	 * @return SaleDTO with sale details
	 * @throws ApplicationException In case no sale exists with given number
	 */
	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public SaleDTO getSaleDetails(int num) throws ApplicationException {
		return saleHandler.getSaleDetails(num);
	}

}