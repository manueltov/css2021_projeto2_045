package controller.web.inputController.actions;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.services.ICustomerServiceRemote;
import presentation.web.model.NewCustomerModel;


/**
 * Handles the novo cliente event
 * 
 * @author fmartins
 *
 */
@Stateless
public class NewCustomerAction extends Action {
	
	@EJB private ICustomerServiceRemote addCustomerService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		NewCustomerModel model = new NewCustomerModel();
		model.setCustomerService(addCustomerService);
		request.setAttribute("model", model);
		request.getRequestDispatcher("/addCustomer/newCustomer.jsp").forward(request, response);
	}

}
