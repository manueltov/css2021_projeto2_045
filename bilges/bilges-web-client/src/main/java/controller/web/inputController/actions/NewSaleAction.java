package controller.web.inputController.actions;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import presentation.web.model.NewSaleModel;


/**
 * Handles the novo cliente event
 * 
 * @author fmartins
 *
 */
@Stateless
public class NewSaleAction extends Action {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		NewSaleModel model = new NewSaleModel();
		request.setAttribute("model", model);
		request.getRequestDispatcher("/processSale/newSale.jsp").forward(request, response);
	}

}
