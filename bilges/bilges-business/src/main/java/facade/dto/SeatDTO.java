package facade.dto;

/**
 * A class DTO representing a simplified version of a Seat (business side class)
 */
public class SeatDTO {

	private String seatLetter;
	private int seatNumber;

	/**
	 * Creates a new Seat DTO given a seatLetter and seatNumber
	 * @param seatLetter the seat's seat letter
	 * @param seatNumber the seat's seat number
	 */
	public SeatDTO (String seatLetter, int seatNumber) {
		this.seatLetter = seatLetter;
		this.seatNumber = seatNumber;
	}

	/**
	 * Returns the letter of the seat
	 * @return The letter of the seat
	 */
	public String getSeatLetter () {
		return seatLetter;
	}
	
	/**
	 * Returns the number of the seat
	 * @return The number of the seat
	 */
	public int getSeatNumber () {
		return seatNumber;
	}
	
	@Override
	public String toString () {
		return seatLetter + "-" + seatNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((seatLetter == null) ? 0 : seatLetter.hashCode());
		result = prime * result + seatNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatDTO other = (SeatDTO) obj;
		if (seatLetter == null) {
			if (other.seatLetter != null)
				return false;
		} else if (!seatLetter.equals(other.seatLetter))
			return false;
		if (seatNumber != other.seatNumber)
			return false;
		return true;
	}
	
	

}
