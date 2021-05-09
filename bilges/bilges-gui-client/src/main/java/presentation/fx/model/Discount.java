package presentation.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Discount {
    private final StringProperty name = new SimpleStringProperty();
    private final int discountCode;

    public Discount(String name, int code) {
        setName(name);
        discountCode = code;
    }
    
    public final StringProperty nameProperty() {
        return this.name;
    }

    public final String getName() {
        return this.nameProperty().get();
    }

    public final void setName(final String name) {
        this.nameProperty().set(name);
    }
    
	public int getDiscountCode() {
		return discountCode;
	}
    
    @Override
    public String toString(){
    	return getName();
    }
    
}
