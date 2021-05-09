package business.reservation;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import business.paymentdetails.PaymentDetails;
import business.ticket.Ticket;
import business.ticket.TicketStatus;

/**
 * Entity implementation class for Entity: Reservation
 * Class that represents a reservation of tickets.
 *
 */
@Entity
public class Reservation implements Serializable {
	
	@Id @GeneratedValue
	private int id;
	
	@OneToMany @JoinColumn
	private List<Ticket> tickets = new ArrayList<>();
	
	@Column
	private double totalPrice;
	
	@Column (nullable = false)
	private String userEmail;
	
	@Embedded
	private PaymentDetails paymentDetails;
	
	@Enumerated(STRING) @Column (nullable = false)
	private ReservationStatus status = ReservationStatus.UNPAID;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor needed by JPA.
	 */
	public Reservation() {
	}
	
	/**
	 * Constructs a Reservation given its data
	 * @param userEmail The reservation's requester user email
	 * @param tickets The reserved tickets 
	 */
	public Reservation (String userEmail, List<? extends Ticket> tickets) {
		this.userEmail = userEmail;
		this.tickets.addAll(tickets);
		for(Ticket ticket: tickets)
			this.totalPrice += ticket.getPrice();
		this.paymentDetails = new PaymentDetails(this);
	}
	
	/**
	 * Adds a ticket to the reservation
	 * @param ticket The ticket to add
	 */
	public void addTicket (Ticket ticket) {
		tickets.add(ticket);
		totalPrice += ticket.getPrice();
	}
	
	/**
	 * Returns the reservation's total price
	 * @return The reservation's total price
	 */
	public double getTotalPrice () {
		return totalPrice;
	}
	
	/**
	 * Returns the reservation's id
	 * @return The reservation's id
	 */
	public int getId() {
		return this.id;
	}
		
	/**
	 * Returns the reservation's payment details
	 * @return The reservation's payment details
	 */
	public PaymentDetails getPaymentDetails () {
		return paymentDetails;
	}
	
	/**
	 * MOCK METHOD that changes the reservation's and its tickets' states to PAID.
	 * It is not used anywhere in code.
	 */
	public void pay () {
		this.status = ReservationStatus.PAID;
		for (Ticket t : tickets)
			t.setStatus(TicketStatus.PAID);
	}
	

}
