package presentation.fx.inputcontroller;

import java.util.function.UnaryOperator;

import facade.exceptions.ApplicationException;
import facade.exceptions.CustomerWithVatAlreadydExistsException;
import facade.exceptions.VatInvalidException;
import facade.services.ICustomerServiceRemote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import presentation.fx.model.Discount;
import presentation.fx.model.NewCustomerModel;

public class NewCustomerController extends BaseController {

	@FXML
	private TextField vatNumberTextField;

	@FXML
	private TextField designationTextField;

	@FXML
	private TextField phoneNumberTextField;

	@FXML
	private ComboBox<Discount> discountTypeComboBox;

	private NewCustomerModel model;

	private ICustomerServiceRemote customerService;

	public void setCustomerService(ICustomerServiceRemote customerService) {
		this.customerService = customerService;
	}

	public void setModel(NewCustomerModel model) {
		this.model = model;
		designationTextField.textProperty().bindBidirectional(model.designationProperty());
		vatNumberTextField.textProperty().bindBidirectional(model.vatNumberProperty(), new NumberStringConverter());
		phoneNumberTextField.textProperty().bindBidirectional(model.phoneNumberProperty(), new NumberStringConverter());   	
		discountTypeComboBox.setItems(model.getDiscounts());   
		discountTypeComboBox.setValue(model.getSelectedDiscount());
	}

	@FXML
	private void initialize() {
		UnaryOperator<Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("[0-9]*")) { 
				return change;
			}
			return null;
		};

		vatNumberTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
				0, integerFilter));
		phoneNumberTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
				0, integerFilter));
	}

	@FXML
	void createCustomerAction(ActionEvent event) {
		String errorMessages = validateInput();
		

		if (errorMessages.length() == 0) {
			try {
				customerService.addCustomer(model.getVatNumber(), 
						model.getDesignation(), model.getPhoneNumber(), model.getSelectedDiscount().getDiscountCode()); 
				model.clearProperties();
				showInfo(i18nBundle.getString("new.customer.success"));
			} catch (VatInvalidException e) {
				showError(i18nBundle.getString("new.customer.error.vatInvalid") + ": " + e.getMessage());
			} catch (CustomerWithVatAlreadydExistsException e) {
				showError(i18nBundle.getString("new.customer.error.vatExists") + ": " + e.getMessage());
			} catch (ApplicationException e) {
				showError(i18nBundle.getString("new.customer.error.adding") + ": " + e.getMessage());
			}
		} else
			showError(i18nBundle.getString("new.customer.error.validating") + ":\n" + errorMessages);
	}

	private String validateInput() {	
		StringBuilder sb = new StringBuilder();
		String designation = model.getDesignation();
		if (designation == null || designation.length() == 0)
			sb.append(i18nBundle.getString("new.customer.invalid.designation"));
		if (model.getVatNumber() == 0) {
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(i18nBundle.getString("new.customer.invalid.VATNumber"));
		}
		if (model.getSelectedDiscount() == null) {
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(i18nBundle.getString("new.customer.invalid.discount"));
		}

		return sb.toString();
	}

	@FXML
	void discountTypeSelected(ActionEvent event) {
		model.setSelectedDiscount(discountTypeComboBox.getValue());
	}

}
