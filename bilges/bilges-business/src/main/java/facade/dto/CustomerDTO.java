package facade.dto;

import java.io.Serializable;

public class CustomerDTO implements Serializable {

	private static final long serialVersionUID = -4087131153704256744L;
	private final int vatNumber;
	private final String designation;
	private final int id;

	public CustomerDTO(int vatNumber, String designation, int id) {
		this.vatNumber = vatNumber;
		this.designation = designation;
		this.id = id;
	}
	
	public String getDesignation() {
		return designation;
	}
	
	public int getId() {
		return id;
	}
	
	public int getVatNumber() {
		return vatNumber;
	}
}