package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.repositories.PharmacyRepository;

import java.util.List;

@Service("pharmacyService")
public class PharmacyServiceImpl  implements PharmacyService{

    private PharmacyRepository pharmacyRepository;

    @Override
    public Pharmacy findById(Long id) {
        return pharmacyRepository.findById(id).get();
    }

    @Override
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public Pharmacy save(Pharmacy pharmacy) {
        return pharmacyRepository.save(pharmacy);
    }

    @Autowired
    public void setPharmacyRepository(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }
}
