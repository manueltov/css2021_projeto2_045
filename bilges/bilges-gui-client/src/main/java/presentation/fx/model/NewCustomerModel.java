package presentation.fx.model;

import facade.exceptions.ApplicationException;
import facade.services.ICustomerServiceRemote;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NewCustomerModel {

	private final StringProperty designation;
	private final IntegerProperty vatNumber;
	private final IntegerProperty phoneNumber;
	private final ObjectProperty<Discount> selectedDiscount;
	private final ObservableList<Discount> discounts;

	public NewCustomerModel(ICustomerServiceRemote cs) {
		designation = new SimpleStringProperty();
		vatNumber = new SimpleIntegerProperty();
		phoneNumber = new SimpleIntegerProperty();
		this.discounts = FXCollections.observableArrayList();
		try {
			cs.getDiscounts().forEach(d -> discounts.add(new Discount(d.getDescription(), d.getId())));
		} catch (ApplicationException e) {
			// no discounts added
		}
		selectedDiscount = new SimpleObjectProperty<>(null);
	}

	public ObjectProperty<Discount> selectedDiscountProperty() {
		return selectedDiscount;
	}

	public final Discount getSelectedDiscount() {
		return selectedDiscount.get();
	}

	public final void setSelectedDiscount(Discount d) {
		selectedDiscount.set(d);
	}

	public ObservableList<Discount> getDiscounts() {
		return discounts;
	}

	public String getDesignation() {
		return designation.get();
	}

	public StringProperty designationProperty() {
		return designation;
	}

	public int getVatNumber() {
		return vatNumber.get();
	}

	public IntegerProperty vatNumberProperty() {
		return vatNumber;
	}	

	public int getPhoneNumber() {
		return phoneNumber.get();
	}

	public IntegerProperty phoneNumberProperty() {
		return phoneNumber;
	}	

	public void clearProperties() {
		designation.set("");
		vatNumber.set(0);
		phoneNumber.set(0);
		selectedDiscount.set(null);
	}
}
