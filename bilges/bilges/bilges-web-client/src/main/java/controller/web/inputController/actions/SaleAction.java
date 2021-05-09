package controller.web.inputController.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.dto.SaleDTO;
import presentation.web.model.AddProductToSaleModel;

public abstract class SaleAction extends Action{

	/**
	 * Strategy method for processing each request
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException;
	

	/**
	 * Updates the sale model object with data from the SaleDTO
	 * 
	 * @param model The current model
	 * @param saleDTO The saleDTO
	 */
	protected void updateModel(AddProductToSaleModel model, SaleDTO saleDTO) {
		// fill it with data from the request
		model.setSale(Integer.toString(saleDTO.getNumber()));
		model.setCustomer(saleDTO.getCustomerDesignation());
		model.setTotal(Double.toString(saleDTO.getTotal()));
	}	

}
