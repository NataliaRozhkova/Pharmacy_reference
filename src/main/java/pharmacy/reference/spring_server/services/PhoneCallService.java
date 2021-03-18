package pharmacy.reference.spring_server.services;

import pharmacy.reference.spring_server.entitis.District;
import pharmacy.reference.spring_server.entitis.PhoneCall;

import java.util.Date;
import java.util.List;

public interface PhoneCallService {
    List<PhoneCall> findAll();

    PhoneCall findById(Long id);

    PhoneCall save(PhoneCall phoneCall);

    List<PhoneCall> findByDate(Date date);

    List<PhoneCall> findByPeriod(Date startDate, Date finishDate);

}
