package business.event;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import business.ticket.DailyTicket;
import facade.dto.DayPeriod;
import facade.dto.SeatDTO;

/**
 * Entity implementation class for Entity: EventDay
 * A class representing a day of an event
 */
@Entity
public class EventDay implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue (strategy = GenerationType.AUTO)
	private int id;

	@Embedded
	private DayPeriod day;

	@OneToMany(cascade = ALL)
	private List<DailyTicket> dailyTickets;

	/**
	 * Constructor needed by JPA.
	 */
	public EventDay () {
	}

	/**
	 * Creates a new EventDay given a day period
	 * @param day The day period
	 */
	public EventDay (DayPeriod day) {
		this.day = day;
	}

	/**
	 * Returns the DayPeriod 
	 * @return the DayPeriod 
	 */
	public DayPeriod getDayPeriod () {
		return this.day;
	}

	/**
	 * Returns the date of the event day
	 * @return the date of the event day 
	 */
	public LocalDate getDate () {
		return day.getDate();
	}

	/**
	 * Returns the list of daily tickets associated with the event day
	 * @return the list of daily tickets associated with the event day 
	 */
	public List<DailyTicket> getDailyTickets () {
		return dailyTickets;
	}

	/**
	 * Returns the start time of the event day
	 * @return the start time of the event day 
	 */
	public LocalTime getStartTime () {
		return day.getStartTime();
	}

	/**
	 * Returns the end time of the event day
	 * @return the end time of the event day 
	 */
	public LocalTime getEndTime () {
		return day.getEndTime();
	}

	/**
	 * Returns the id of the event day
	 * @return The id of the event day
	 */
	public int getId() {
		return id;
	}

	/**
	 * Adds one or more daily tickets to the event day
	 */
	public void addDailyTickets(Collection<DailyTicket> dt) {
		this.dailyTickets.addAll(dt);
	}

	//TODO: make query instead
	public List<DailyTicket> getTickets(List<SeatDTO> seats) {
		List<DailyTicket> result = new ArrayList<>();
		for(DailyTicket t: dailyTickets) {
			for(SeatDTO seat: seats) {
				if(t.getSeat() != null && t.getSeat().getSeatLetter().equals(seat.getSeatLetter()) &&
						t.getSeat().getSeatNumber() == seat.getSeatNumber()){
					result.add(t);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the number available daily tickets.
	 *
	 * @return the number of available daily tickets in the given event day
	 */
	//TODO make query instead
	public int getNumberOfFreeDailyTickets() {
		return (int) dailyTickets.stream().filter(t -> t.isAvailable()).count();
	}


	@Override
	public String toString () {
		return getDate() + ", " + getStartTime() + " - " + getEndTime();
	}



}