package business.handlers;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.event.Event;
import business.event.EventCatalog;
import business.event.EventType;
import business.utils.DateUtils;
import business.venue.SeatedVenue;
import business.venue.Venue;
import business.venue.VenueCatalog;
import facade.exceptions.ApplicationException;
import facade.exceptions.EventHasAssignedVenueException;
import facade.exceptions.EventNotFoundException;
import facade.exceptions.InvalidTicketPriceException;
import facade.exceptions.InvalidTicketSaleDateException;
import facade.exceptions.InvalidVenueException;
import facade.exceptions.OccupiedVenueException;

/**
 * Handles the assign venue to event use case
 *
 */
@Stateless
public class AssignVenueHandler {

	/**
	 * Entity manager factory for accessing the persistence service 
	 */
	@EJB
	private EntityManagerFactory emf;
	@EJB
	private Event currentEvent;

	/**
	 * Creates a handler for assign venue to event use case given
	 * the application's entity manager factory
	 * @param emf The entity manager factory of the application
	 */
	public AssignVenueHandler (EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Given the required venue and event data, assigns a venue to an event, and creates the
	 * event tickets.
	 * 
	 * @param eventDesignation The event's name
	 * @param venueName The venue's name
	 * @param sellingStart The date in which the selling of the tickets begins
	 * @param ticketPrice The daily ticket price
	 * @throws ApplicationException if an error occurs while attempting to assign the venue to the event
	 */
	public void assignVenueToEvent (String eventDesignation, String venueName, 
			LocalDate sellingStart, double ticketPrice) throws ApplicationException {

		EntityManager em = emf.createEntityManager();
		EventCatalog eventCatalog = new EventCatalog(em);
		VenueCatalog venueCatalog = new VenueCatalog(em);

		try {
			em.getTransaction().begin();

			Event event = eventCatalog.getEvent(eventDesignation);
			if (event == null)
				throw new EventNotFoundException ("Could not find an event with designation \"" + eventDesignation + "\"");
			if (event.hasVenue())
				throw new EventHasAssignedVenueException ("The event \"" + eventDesignation + "\" already has a venue (\"" + event.getVenue().getName() + "\") assigned to it");

			if (ticketPrice < 0)
				throw new InvalidTicketPriceException ("Invalid ticket price: ticket prices must be positive");
			checkSellingDateIsValidForEvent(sellingStart, event);

			Venue venue = venueCatalog.getVenue(venueName);
			if (venue == null)
				throw new ApplicationException ("Could not find a venue with name \"" + venueName + "\"");

			checkVenueValidForEventAndIsFree(event,venue);
		
			event.setVenue(venue);
			event.createTickets(ticketPrice);
			event.setSellingStartDate(sellingStart);

			em.getTransaction().commit();

			this.currentEvent = event;
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error assigning venue named \"" + venueName + "\" to the event \"" + eventDesignation + "\"", e);

		} finally {
			em.close();
		}		

	}

	private void checkSellingDateIsValidForEvent(LocalDate sellingStart, Event event) throws InvalidTicketSaleDateException {
		if (sellingStart.isBefore(DateUtils.getMockCurrentDate()))
			throw new InvalidTicketSaleDateException ("Error: The specified ticket sale date must be in the future");

		if (!sellingStart.isBefore(event.getFirstDay())) 
			throw new InvalidTicketSaleDateException ("Error: The specified ticket sale date is not prior to the event's first day");		
	}

	private void checkVenueValidForEventAndIsFree(Event event, Venue venue) throws ApplicationException {
		EventType type = event.getEventType();
		if (venue instanceof SeatedVenue) {
			if (!type.isSeated())
				throw new InvalidVenueException ("Venue \"" + venue.getName() + "\" only hosts seated events, and therefore cannot host \"" + type + "\" event types");
			if (venue.getCapacity() > type.getCapacity())
				throw new InvalidVenueException ("Venue \"" + venue.getName() + "\"'s capacity (" + venue.getCapacity() + ") exceeds the event's type max lotation (" 
						+ type.getCapacity() + ")");
		} else {
			if (type.isSeated())
				throw new InvalidVenueException ("Venue \"" + venue.getName() + "\" only hosts standing events, and therefore cannot host \"" + type + "\" event types");
		}	
		if (!venue.isFree(event.getEventDayPeriods()))
			throw new OccupiedVenueException ("The venue \"" + venue.getName() + "\" is occupied at the time of the specified event days.");

	}

	/**
	 * Allows the event to which the venue was assigned to sell ticket passes, with the price
	 * passed as a parameter.
	 * 
	 * @param ticketPassCost The ticket pass price
	 * @throws ApplicationException if an error occurs while updating the event's ticket pass permissions
	 */
	public void allowTicketPasses (double ticketPassCost) throws ApplicationException {
		EntityManager em = emf.createEntityManager();

		try {
			if (ticketPassCost < 0)
				throw new InvalidTicketPriceException ("Invalid ticket pass price: ticket prices must be positive");

			em.getTransaction().begin();

			currentEvent = em.find(Event.class, currentEvent.getId());
			currentEvent.setTicketPassPermission(true);
			currentEvent.setTicketPassPrice(ticketPassCost);

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) 
				em.getTransaction().rollback();
			throw new ApplicationException("Error while trying to attribute ticket pass permission to event \"" + currentEvent.getDesignation() + "\"", e);
		} finally {
			em.close();
		}

	}

	/**
	 * Returns a list with the all the existing venue names
	 * @return a list with the all the existing venue names
	 */
	public List<String> startVenueAssignment() throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		VenueCatalog venueCatalog = new VenueCatalog(em);
		try {
			em.getTransaction().begin();
			List<String> result = venueCatalog.getAllVenueNames();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) 
				em.getTransaction().rollback();
			throw new ApplicationException("Error fetching venue names", e);
		} finally {
			em.close();
		}
	}

}