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
 * Handles the addition of product to sale event.
 * If the request is valid, it dispatches it to the domain model (the application's business logic)
 * Notice as well the use of an helper class to assist in the jsp response. 
 * 
 * @author alopes
 *
 */
@Stateless
public class AddProductToSaleAction extends SaleAction {

	@EJB private ISaleServiceRemote saleService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		// if the request does not have a sale number then this action is not possible
		NewSaleModel saleModel = new NewSaleModel();
		if (!isInt(saleModel,request.getParameter("venda"), "Não existe uma venda corrente.")) {
			request.setAttribute("model", saleModel);
			request.getRequestDispatcher("/processSale/newSale.jsp").forward(request, response);
			return;
		}

		// if the request does have a sale number 
		AddProductToSaleModel model = new AddProductToSaleModel();
		updateModel(model, request);
		request.setAttribute("model", model);

		boolean productSaleCreated = false;
		SaleDTO saleDTO = new SaleDTO(intValue(model.getSale()));

		if (isInputValid(model)) {
			try {
				saleDTO = saleService.addProductToSale(intValue(model.getSale()),
						intValue(model.getProduct()), intValue(model.getQuantity())); 
				model.clearFormFields();
				productSaleCreated = true;
			} catch (ApplicationException e) {
				model.addMessage("Erro a criar a nova entrada da venda. " + e.getMessage());
			}
		} else {
			model.addMessage("Erro de validação dos dados da entrada da venda.");
		}

		if (!productSaleCreated) {
			try {
				saleDTO = saleService.getSaleDetails(intValue(model.getSale()));
			} catch (ApplicationException e) {
				model.addMessage("Erro ao obter os detalhes da venda. " + e.getMessage());
			}
		}
		updateModel(model, saleDTO);
		request.getRequestDispatcher("/processSale/addProductToSale.jsp").forward(request, response);				
	}

	/**
	 * Validate the input request
	 * 
	 * @param nch The model object to be field.
	 * @return True if the fields are inputed correctly
	 */	
	private boolean isInputValid(AddProductToSaleModel model) {		

		// check if product  is filled and a valid number
		boolean result = isFilled(model, model.getProduct(), "Falta indicar o código do produto.")
				&&  isInt(model, model.getQuantity(), "Código tem caracteres inválidos");

		// check if quantity is filled and a valid number
		result &= isFilled(model, model.getQuantity(), "Falta indicar a quantidade.")
				&& isInt(model, model.getQuantity(), "Quantidade tem caracteres inválidos");

		return result;
	}	


	/**
	 * Updates the sale model object with data from the request
	 * 
	 * @param model The current model
	 * @param request The request
	 */
	private void updateModel(AddProductToSaleModel model, HttpServletRequest request) {
		// fill it with data from the request
		model.setSale(request.getParameter("venda"));
		model.setProduct(request.getParameter("produto"));
		model.setQuantity(request.getParameter("quantidade"));
	}	

}
