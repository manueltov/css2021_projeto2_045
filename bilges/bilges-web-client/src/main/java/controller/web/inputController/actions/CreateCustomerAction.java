package controller.web.inputController.actions;

import java.io.IOException;
import java.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.exceptions.ApplicationException;
import facade.exceptions.CustomerWithVatAlreadydExistsException;
import facade.exceptions.VatInvalidException;
import facade.services.ICustomerServiceRemote;
import presentation.web.model.NewCustomerModel;

/**
 * Handles the create client event.
 * If the request is valid, it dispatches it to the domain model (the application's business logic)
 * Notice as well the use of an helper class to assist in the jsp response. 
 * 
 * @author fmartins
 * @author alopes
 *
 */
@Stateless
public class CreateCustomerAction extends Action {

	@EJB private ICustomerServiceRemote customerService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		NewCustomerModel model = createHelper(request);
		request.setAttribute("model", model);

		if (validInput(model)) {
			try {
				customerService.addCustomer(intValue(model.getVATNumber()), 
						model.getDesignation(), intValue(model.getPhoneNumber()), intValue(model.getDiscountType()));
				
				// to illustrate the use of date
				LocalDate birthDate = LocalDate.parse(model.getBirthDate());
				LocalDate now = LocalDate.now();
				if (birthDate.getMonth().equals(now.getMonth()) && birthDate.getDayOfMonth() == now.getDayOfMonth()) {			
					model.addMessage("Faz hoje anos? Parabéns!");
				}
				
				model.clearFields();
				model.addMessage("Cliente adicionado com sucesso.");
			} 
			catch (VatInvalidException e) {
				model.addMessage("Erro ao adicionar cliente. NPC inválido.");
			}
			catch (CustomerWithVatAlreadydExistsException e) {
				model.addMessage("Erro ao adicionar cliente. Cliente com NPC dado já existe.");
			}
			catch (ApplicationException e) {
				model.addMessage("Erro ao adicionar cliente. " + e.getMessage());
			}
		} 
		else {
			model.addMessage("Erro de validação dos dados do cliente.");
		}

		request.getRequestDispatcher("/addCustomer/newCustomer.jsp").forward(request, response);
	}


	private boolean validInput(NewCustomerModel model) {

		// check if designation is filled
		boolean result = isFilled(model, model.getDesignation(), "A designação tem de estar preenchida.");

		// check if VATNumber is filled and a valid number
		result &= isFilled(model, model.getVATNumber(), "O NPC tem que estar preenchido")
				&& isInt(model, model.getVATNumber(), "O NPC contem caracteres inválidos");

		// check in case phoneNumber is filled if it contains a valid number
		if (!model.getPhoneNumber().equals(""))
			result &= isInt(model, model.getPhoneNumber(), "O número de telefone tem caracteres inválidos");

		// check if discount type is filled and a valid number
		result &= isFilled(model, model.getDiscountType(), "O desconto tem de estar preenchido") 
				&& isInt(model, model.getDiscountType(), "O desconto contem caracteres inválidos");

		// check in case birtdate is filled if is a valid date
		result &= isDate(model, model.getBirthDate(), "A data de nascimento não é válida");
		
		return result;
	}


	private NewCustomerModel createHelper(HttpServletRequest request) {
		// Create the object model
		NewCustomerModel model = new NewCustomerModel();
		model.setCustomerService(customerService);

		// fill it with data from the request
		model.setDesignation(request.getParameter("designacao"));
		model.setVATNumber(request.getParameter("npc"));
		model.setPhoneNumber(request.getParameter("telefone"));
		model.setDiscountType(request.getParameter("desconto"));
		model.setBirthDate(request.getParameter("dataNascimento"));

		return model;
	}	
}
