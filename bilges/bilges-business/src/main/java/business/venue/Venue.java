package business.venue;


import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import business.event.Event;
import business.event.EventDay;
import business.ticket.DailyTicket;
import facade.dto.DayPeriod;

/**
 * Entity implementation class for Entity: Venue
 *
 */
@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
	@NamedQuery(name=Venue.FIND_BY_NAME, query="SELECT v FROM Venue v WHERE v.name = :" + Venue.VENUE_NAME),
	@NamedQuery(name=Venue.GET_ALL_NAMES, query="SELECT v.name FROM Venue v"),
})
@DiscriminatorColumn (name = "VENUE_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Venue implements Serializable {
	
	public static final String FIND_BY_NAME = "Venue.findByName";
	public static final String GET_ALL_NAMES = "Venue.getAllNames";
	public static final String VENUE_NAME = "name";
	
	@Id @GeneratedValue(strategy = AUTO)
	protected int id;
	
	@Column (nullable = false, unique = true)
	protected String name;
	
	@Column
	protected int capacity;
	
	@OneToMany @JoinColumn 
	private List<EventDay> eventDays = new ArrayList<>();
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor needed by JPA.
	 */
	protected Venue() {
	}
	
	/**
	 * Constructor for a new Venue, that is not (yet) used.
	 * @param name The name of the venue
	 */
	protected Venue (String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the venue 
	 * @return the name of the vnue 
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the capacity of any type of Venue.
	 * @return the capacity of any type of the venue.
	 */
	public abstract int getCapacity ();
	
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * Returns the list of event days in which the venue is used
	 * @return the list of event days in which the venue is used
	 */
	public List<EventDay> getEventDays() {
		return eventDays;
	}
	
	/**
	 * Assigns event days to the venue
	 * @param days The list of event days we're assigning to the venue
	 */
	public void addEventDays (List<EventDay> days) {
		eventDays.addAll(days);
	}

	/**
	 * Provides the list of tickets for a day event in a venue 
	 *  
	 * @param event
	 * @param day
	 * @param ticketPrice
	 * @return the list of tickets
	 */
	public abstract List<DailyTicket> createTicketsDayEvent(Event event, EventDay day, double ticketPrice);
	/**
	 * Checks if there is any date/hour period intersection between the given list
	 * and that of the events already scheduled to the venue.
	 * 
	 * @param dayPeriods	a list of day periods
	 * @return whether the venue is free in all periods in the list
	 */
	//TODO: make this search in the database with a query!!!!
	public boolean isFree(List<DayPeriod> dayPeriods) {
		if (eventDays.isEmpty())
			return true;
		for (DayPeriod day : dayPeriods) {
			for (EventDay eday : eventDays) {
				if (day.overlaps(eday.getDayPeriod()))
					return false;
			}
		}
		return true;
	}
   
}
