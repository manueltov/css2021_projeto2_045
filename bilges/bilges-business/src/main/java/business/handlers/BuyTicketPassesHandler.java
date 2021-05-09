package business.handlers;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.event.Event;
import business.event.EventCatalog;
import business.event.EventDay;
import business.paymentdetails.PaymentDetails;
import business.reservation.Reservation;
import business.ticket.DailyTicket;
import business.ticket.TicketPass;
import business.ticket.TicketStatus;
import business.utils.DateUtils;
import facade.dto.MBPaymentInfoDTO;
import facade.exceptions.ApplicationException;
import facade.exceptions.EventNotFoundException;
import facade.exceptions.InvalidTicketPurchaseException;

/**
 * Handles the buy ticket passes use case
 */
@Stateless
public class BuyTicketPassesHandler {
	
	/**
	 * Entity manager factory for accessing the persistence service 
	 */
	@EJB
	private EntityManagerFactory emf;
	@EJB
	private Event selectedEvent;
	@EJB
	private int numberTicketsPassProvided;

	/**
	 * Creates a handler for the buy ticket passes use case given
	 * the application's entity manager factory
	 * @param emf The entity manager factory of the application
	 */
	public BuyTicketPassesHandler (EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Returns the number of ticket passes available for the event with the given designation
	 * 
	 * @param eventDesignation the event designation
	 * @return the number of ticket passes available for the event with the given designation
	 * @throws ApplicationException if an error occurs while attempting to fetch the number of ticket
	 * of ticket passes available
	 */
	public int getNumberOfTicketPasses (String eventDesignation) throws ApplicationException {
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			EventCatalog eventCatalog = new EventCatalog(em);

			Event e = eventCatalog.getEvent(eventDesignation);
			if (e == null) 
				throw new EventNotFoundException ("Could not find an event named " + eventDesignation);	
			if (e.getSellingDateStart().isAfter(DateUtils.getMockCurrentDate()))
				throw new InvalidTicketPurchaseException ("Tickets for the event \"" + eventDesignation + "\" are not available yet");
			if (!e.allowsTicketPass()) 
				throw new InvalidTicketPurchaseException ("The event named \"" + eventDesignation + "\" does not support Ticket Pass selling.");

			this.numberTicketsPassProvided = e.getNumberOfAvailableTicketPass();
			em.getTransaction().commit();
			this.selectedEvent = e;
			return numberTicketsPassProvided;

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error trying to fetch number of available daily passes for the event \"" + eventDesignation + "\"", e);

		} finally {
			em.close();
		}

	}


	/**
	 * Makes a reservation for a given number of ticket passes for the previously named event,
	 * and returns the payment details
	 * 
	 * @param nPasses Number of desired ticket passes
	 * @param userEmail The user's email adress
	 * @return The reservation's payment details
	 * @throws ApplicationException if an error occurs attempting to make the reservation
	 */
	public MBPaymentInfoDTO chooseTicketPassQuantity (int nPasses, String userEmail) throws ApplicationException {

		if (nPasses > numberTicketsPassProvided)
			throw new ApplicationException ("Error: Specified " + nPasses + " ticket passes when there are only " + numberTicketsPassProvided + " available.");

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			selectedEvent = em.merge(selectedEvent);
			
			List<TicketPass> tickets = new ArrayList<>();
			for (int i = 0; i < nPasses; i++) {
				TicketPass pass = new TicketPass (selectedEvent);
				em.persist(pass);
				tickets.add(pass);

				for (EventDay d : selectedEvent.getEventDays()) {
					DailyTicket t = getSingleDailyTicket (d);
					pass.addDailyTicket(t);
				}
				pass.setStatus(TicketStatus.RESERVED);
			}

			Reservation res = new Reservation (userEmail, tickets);
			em.persist(res);
			PaymentDetails payment = res.getPaymentDetails();

			em.getTransaction().commit();
			
			return new MBPaymentInfoDTO(payment.getEntity(), payment.getReference(), payment.getValue());

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error creating a reservation for a ticket pass, for the event \"" + selectedEvent.getDesignation() + "\"", e);

		} finally {
			em.close();
		}

	}

	/**
	 * Returns the first daily ticket it finds in the given Event Day
	 * 
	 * @param d The EventDay
	 * @return An available daily ticket in the event day d.
	 * @throws ApplicationException If an error occurs attempting to find a free daily ticket.
	 */
	private DailyTicket getSingleDailyTicket(EventDay d) throws ApplicationException {
		for (DailyTicket t : d.getDailyTickets()) {
			if (t.isAvailable())
				return t;
		}
		throw new ApplicationException ("Could not find any free daily tickets for the date " + d.getDate() + " (Event: " + selectedEvent.getDesignation() + ")");
	}

}
