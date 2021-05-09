package facade.services;

import javax.ejb.Remote;

import facade.dto.SaleDTO;
import facade.exceptions.ApplicationException;

@Remote
public interface ISaleServiceRemote {

	public SaleDTO newSale (int vat) throws ApplicationException;
	
	public SaleDTO addProductToSale (int num, int prodCod, double qty) throws ApplicationException;
	
	public SaleDTO getSaleDetails(int num) throws ApplicationException;
	
}
