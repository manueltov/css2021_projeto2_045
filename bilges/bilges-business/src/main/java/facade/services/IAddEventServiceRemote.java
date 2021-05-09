package facade.services;

import javax.ejb.Remote;
import java.util.List;
import facade.dto.DayPeriod;
import facade.exceptions.ApplicationException;

@Remote
public interface IAddEventServiceRemote {
	public void addEvent (String designation, String type, int companyID, List<DayPeriod> days) throws ApplicationException;
	public List<String> createNewEvent ();
}


