package facade.dto;

import java.io.Serializable;

public class DiscountDTO implements Serializable {

	private static final long serialVersionUID = -4087131153704256744L;

	private final int id;
	private final String description;

	public DiscountDTO(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
}