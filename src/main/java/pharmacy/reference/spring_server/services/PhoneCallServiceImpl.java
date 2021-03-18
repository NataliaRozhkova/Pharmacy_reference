package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.reference.spring_server.entitis.District;
import pharmacy.reference.spring_server.entitis.PhoneCall;
import pharmacy.reference.spring_server.repositories.PhoneCallRepository;

import java.util.Date;
import java.util.List;

@Service("phoneCallService")
public class PhoneCallServiceImpl implements PhoneCallService{
    private PhoneCallRepository phoneCallRepository;

    @Override
    public List<PhoneCall> findAll() {
        return phoneCallRepository.findAll();
    }

    @Override
    public PhoneCall findById(Long id) {
        return phoneCallRepository.findById(id).get();
    }

    @Override
    public PhoneCall save(PhoneCall phoneCall) {
        return phoneCallRepository.save(phoneCall);
    }

    @Override
    public List<PhoneCall> findByDate(Date date) {
        return phoneCallRepository.findByDate(date);
    }

    @Override
    public List<PhoneCall> findByPeriod(Date startDate, Date finishDate) {
        return phoneCallRepository.findByPeriod(startDate, finishDate);
    }

    @Autowired
    public void setPhoneCallRepository(PhoneCallRepository phoneCallRepository) {
        this.phoneCallRepository = phoneCallRepository;
    }
}
