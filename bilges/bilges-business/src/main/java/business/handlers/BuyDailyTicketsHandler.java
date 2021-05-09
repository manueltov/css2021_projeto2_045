package business.handlers;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.event.Event;
import business.event.EventCatalog;
import business.event.EventDay;
import business.paymentdetails.PaymentDetails;
import business.reservation.Reservation;
import business.ticket.DailyTicket;
import business.ticket.TicketCatalog;
import business.ticket.TicketStatus;
import business.utils.DateUtils;
import business.venue.Seat;
import facade.dto.MBPaymentInfoDTO;
import facade.dto.SeatDTO;
import facade.exceptions.ApplicationException;
import facade.exceptions.EventNotFoundException;
import facade.exceptions.InvalidChosenDateException;
import facade.exceptions.InvalidSeatException;
import facade.exceptions.InvalidTicketPurchaseException;
import facade.exceptions.SeatAlreadyTakenException;

/**
 * Handles the buy daily tickets use case:
 * 	1) getAvailableDates
 *  2) chooseDate
 *  3) chooseSeats
 */
@Stateless
public class BuyDailyTicketsHandler {

	private Event selectedEvent;
	private EventDay selectedEventDay;
	private List<SeatDTO> seatsProvided;

	/**
	 * Entity manager factory for accessing the persistence service 
	 */
	@EJB
	private EntityManagerFactory emf;

	/**
	 * Creates a handler for the buy daily tickets use case given
	 * the application's entity manager factory
	 * @param emf The entity manager factory of the application
	 */
	public BuyDailyTicketsHandler (EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Returns the dates in which the event still has available daily tickets
	 * 
	 * @param eventDesignation The event's name
	 * @return The dates in which the event still has available daily tickets
	 * @throws ApplicationException if an error occurs while fetching the dates
	 */
	public List<LocalDate> getAvailableDates (String eventDesignation) throws ApplicationException {
		EntityManager em = emf.createEntityManager();

		EventCatalog eventCatalog = new EventCatalog(em);

		try {
			em.getTransaction().begin();

			Event e = eventCatalog.getEvent(eventDesignation);
			if (e == null) 
				throw new EventNotFoundException ("Could not find an event named " + eventDesignation);

			if (!e.getEventType().isSeated())
				throw new InvalidTicketPurchaseException ("The event " + e.getDesignation() + " is not an event with individual seats.");

			if (e.getVenue() == null)
				throw new InvalidTicketPurchaseException ("Tickets for the event \"" + eventDesignation + "\" are not available yet, as the event has no assigned venue");

			if (e.getSellingDateStart().isAfter(DateUtils.getMockCurrentDate()))
				throw new InvalidTicketPurchaseException ("Tickets for the event \"" + eventDesignation + "\" are not available yet");

			List<LocalDate> dates = e.getDatesNotSoldOut();
			em.getTransaction().commit();

			this.selectedEvent = e;
			return dates;

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error trying to get available dates for the event \"" + eventDesignation + "\"", e);

		} finally {
			em.close();
		}
	}

	/**
	 * Returns the available seats for a given date of the event
	 * 
	 * @param date The specified date
	 * @return the list of available seats for the given date
	 * @throws ApplicationException if an error occurs while attempting to retrieve the seats
	 */
	public List<SeatDTO> chooseDate (LocalDate date) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		TicketCatalog ticketCatalog = new TicketCatalog(em);

		try {
			if (!selectedEvent.getEventDates().contains(date)) 
				throw new InvalidChosenDateException ("The chosen date does not belong to the event.");

			em.getTransaction().begin();
			selectedEvent = em.merge(selectedEvent);
			EventDay ed = selectedEvent.getEventDay(date);

			if (!selectedEvent.getDatesNotSoldOut().contains(date)) 
				throw new InvalidChosenDateException ("The chosen date has no available tickets."); 

			Date confirmedDate = DateUtils.toDate(date);

			List<SeatDTO> seatDTOs = new ArrayList<>();
			for (Seat s : ticketCatalog.getOrderedSeatsByEventAndDate(selectedEvent, confirmedDate)) {
				seatDTOs.add(new SeatDTO (s.getSeatLetter(), s.getSeatNumber()));
			}
			em.getTransaction().commit();

			this.selectedEventDay = ed;	
			this.seatsProvided = new ArrayList<>(seatDTOs);
			return seatDTOs;

		} catch (Exception e) {
			throw new ApplicationException("Error selecting the desired event date", e);
		}
	}


	/**
	 * Makes a reservation for the given seats and returns the payment details for the reservation.
	 * 
	 * @param seats The list of seats the user wishes to reserve
	 * @param userEmail The user's email
	 * @return The payment details for the reservation
	 * @throws ApplicationException if an error occurs while attempting to make the reservation
	 */
	public MBPaymentInfoDTO chooseSeats (List<SeatDTO> seats, String userEmail) throws ApplicationException {
		EntityManager em = emf.createEntityManager();

		try {
			if(!seatsProvided.containsAll(seats))
				throw new InvalidSeatException ("Could not find one or more seats in the event " + 
						selectedEvent.getDesignation());

			em.getTransaction().begin();
			selectedEventDay = em.merge(selectedEventDay);			

			List<DailyTicket> tickets = selectedEventDay.getTickets(seats);
			for (DailyTicket t : tickets) {
				if (t.isAvailable()) {
					t.setStatus(TicketStatus.RESERVED);
				}else {
					throw new SeatAlreadyTakenException ("The seat " + t.getSeat() + " is already reserved (Date " + selectedEventDay.getDate() + ")");
				}
			} 

			Reservation res = new Reservation(userEmail,tickets);
			em.persist(res);

			PaymentDetails payment = res.getPaymentDetails();
	
			em.getTransaction().commit();
			
			return new MBPaymentInfoDTO(payment.getEntity(), payment.getReference(), payment.getValue());
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error trying to make a reservation for a list of daily tickets", e);

		} finally {
			em.close();
		}
	}



}
