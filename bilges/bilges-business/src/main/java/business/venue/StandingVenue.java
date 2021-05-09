package business.venue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import business.event.Event;
import business.event.EventDay;
import business.ticket.DailyTicket;

/**
 * Entity implementation class for Entity: StandingVenue
 * Subclass of Venue.
 *
 */
@Entity
@DiscriminatorValue(value = "Standing")
public class StandingVenue extends Venue {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor needed by JPA.
	 */
	public StandingVenue() {
	}
	
	@Override
	public int getCapacity() {
		return this.capacity;
	}
	
	/**
	 * Provides the list of tickets for a day event in a venue  (as many tickets as the capacity of the venue)
	 *  
	 * @param event
	 * @param day
	 * @param ticketPrice
	 * @return the list of tickets
	 */
	public List<DailyTicket> createTicketsDayEvent(Event event, EventDay day, double ticketPrice) {
		List<DailyTicket> tickets = new ArrayList<>();
		for (int i = 0; i < getCapacity(); i++)
			tickets.add(new DailyTicket (event, day, ticketPrice));
		return tickets;
	}

   
}
