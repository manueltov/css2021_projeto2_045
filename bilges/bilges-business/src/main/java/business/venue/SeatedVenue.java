package business.venue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import business.event.Event;
import business.event.EventDay;
import business.ticket.DailyTicket;

/**
 * Entity implementation class for Entity: SeatedVenue
 * Subclass of Venue.
 *
 */
@Entity
@DiscriminatorValue(value = "Seated")
public class SeatedVenue extends Venue {
	
	@OneToMany @JoinColumn (name = "VENUE_ID")
	private List<Seat> seats = new ArrayList<>();
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor needed by JPA.
	 */
	public SeatedVenue() {
	}
	
	@Override
	public int getCapacity() {
		return this.seats.size();
	}
	
	/**
	 * Returns a list with all the seats of the seated venue
	 * @return The list of seats of the seated venue 
	 */
	public List<Seat> getSeats () {
		return seats;
	}
	
	/**
	 * Provides the list of tickets for a day event in a seated venue (tickets have seats)
	 *  
	 * @param event
	 * @param day
	 * @param ticketPrice
	 * @return the list of tickets
	 */
	@Override
	public List<DailyTicket> createTicketsDayEvent(Event event, EventDay day, double ticketPrice) {
		List<DailyTicket> tickets = new ArrayList<>();
		for (Seat seat : seats) 
			tickets.add(new DailyTicket (event, seat, day, ticketPrice));
		return tickets;
	}

   
}
