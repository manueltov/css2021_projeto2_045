package facade.services;

import javax.ejb.Remote;
import facade.dto.MBPaymentInfoDTO;
import facade.exceptions.ApplicationException;

@Remote
public interface ITicketPassPurchaseServiceRemote {
	public int startTicketPassReservation (String eventDesignation) throws ApplicationException;
	public MBPaymentInfoDTO chooseTicketPassQuantity (int nPasses, String userEmail) throws ApplicationException;
}
