package facade.services;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import facade.dto.MBPaymentInfoDTO;
import facade.dto.SeatDTO;
import facade.exceptions.ApplicationException;
@Remote
public interface IDailyTicketPurchaseServiceRemote {
	public List<LocalDate> startDailyTicketReservation (String eventDesignation) throws ApplicationException;
	public List<SeatDTO> chooseDate (LocalDate date) throws ApplicationException;
	public MBPaymentInfoDTO chooseSeats (List<SeatDTO> seats, String userEmail) throws ApplicationException;
}
