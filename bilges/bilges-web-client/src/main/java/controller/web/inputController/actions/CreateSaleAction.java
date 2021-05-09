package controller.web.inputController.actions;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.dto.SaleDTO;
import facade.exceptions.ApplicationException;
import facade.services.ISaleServiceRemote;
import presentation.web.model.AddProductToSaleModel;
import presentation.web.model.NewSaleModel;

/**
 * Handles the create sale event.
 * If the request is valid, it dispatches it to the domain model (the application's business logic)
 * Notice as well the use of an helper class to assist in the jsp response. 
 * 
 * @author alopes
 *
 */
@Stateless
public class CreateSaleAction extends SaleAction {

	@EJB private ISaleServiceRemote saleService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		NewSaleModel saleModel = createModel(request);
		AddProductToSaleModel addProductModel =  new AddProductToSaleModel();

		boolean saleCreated = false;
		
		if (isInputValid(saleModel)) {
			try {
				SaleDTO saleDTO = saleService.newSale(intValue(saleModel.getVATNumber())); 
				updateModel(addProductModel,saleDTO);
				saleCreated = true;
			} catch (ApplicationException e) {
				saleModel.addMessage("Erro a criar a venda. " + e.getMessage());
			}
		} 
		else {
			saleModel.addMessage("Erro de validação dos dados.");
		}
		

		if (!saleCreated) {	
			request.setAttribute("model", saleModel);
			request.getRequestDispatcher("/processSale/newSale.jsp").forward(request, response);
		}
		else {
			request.setAttribute("model", addProductModel);			
			request.getRequestDispatcher("/processSale/addProductToSale.jsp").forward(request, response);		
		}
	}

	/**
	 * Validate the input request
	 * 
	 * @param nch The model object to be field.
	 * @return True if the fields are inputed correctly
	 */	
	private boolean isInputValid(NewSaleModel nvh) {		
		// check if VATNumber is filled and a valid number
		return isFilled(nvh, nvh.getVATNumber(), "VAT number must be filled")
				 			&& isInt(nvh, nvh.getVATNumber(), "VAT number with invalid characters");
	}	

	/**
	 * Create the sale model object and fills it with data from the request
	 * 
	 * @param request The current HTTP request
	 * @param app The app object
	 * @return The brand new sale model object
	 */
	private NewSaleModel createModel(HttpServletRequest request) {
		// Create the object model
		NewSaleModel model = new NewSaleModel();

		// fill it with data from the request
		model.setVATNumber(request.getParameter("npc"));
		
		return model;
	}	

}
