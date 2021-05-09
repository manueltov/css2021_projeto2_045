package facade.services;


import javax.ejb.Remote;
import java.time.LocalDate;
import java.util.List;

import facade.exceptions.ApplicationException;

@Remote
public interface IAssignVenueServiceRemote {
	public void assignVenueToEvent (String eventDesignation, String venueName, LocalDate sellingStart, 
			double ticketPrice) throws ApplicationException;
	public void allowTicketPasses (double ticketPassCost) throws ApplicationException;
	public List<String> startVenueAssignment () throws ApplicationException;

}


