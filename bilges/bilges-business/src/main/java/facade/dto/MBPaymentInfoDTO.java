package facade.dto;


public class MBPaymentInfoDTO {

	private String entity;

	private String reference;

	private double value;

	public MBPaymentInfoDTO(String entity, String reference, double value) {
		this.entity = entity;
		this.reference = reference;
		this.value = value;
	}

	public String getEntity() {
		return entity;
	}

	public String getReference() {
		return reference;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Entity "+ entity + ", Ref " + reference + ", Value " + value;
	}
}
