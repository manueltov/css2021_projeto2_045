package facade.dto;

import java.io.Serializable;

public class SaleDTO implements Serializable {

	private static final long serialVersionUID = -4087131153704256744L;
	private final int number;
	private final String customerDesignation;
	private final double total;

	public SaleDTO(int number, String customerDesignation, double total) {
		this.number = number;
		this.customerDesignation = customerDesignation;
		this.total = total;
	}
	
	public SaleDTO(int number) {
		this(number,"",0);
	}

	public SaleDTO() {
		this(0);
	}

	public String getCustomerDesignation() {
		return customerDesignation;
	}
	
	public int getNumber() {
		return number;
	}
	
	public double getTotal() {
		return total;
	}
}