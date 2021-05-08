package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.repositories.PharmacyRepository;

import java.util.List;

@Service("pharmacyService")
public class PharmacyServiceImpl implements PharmacyService {

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

    @Override
    public List<Pharmacy> saveAll(List<Pharmacy> pharmacies) {
        return pharmacyRepository.saveAll(pharmacies);
    }

    @Override
    public List<Pharmacy> findByName(String name) {
        return pharmacyRepository.findByName(name);
    }

    @Override
    public List<Pharmacy> findAllVisible() {
        return pharmacyRepository.findAllVisible();
    }

    @Override
    public List<Pharmacy> findAllByPharmacyChain(Long chainId) {
        return pharmacyRepository.findAllByPharmacyChain(chainId);
    }

    @Override
    public List<Pharmacy> findAllByEmail(String email) {
        return pharmacyRepository.findAllByEmail(email);
    }

    @Override
    public void delete(Long id) {
        pharmacyRepository.delete(findById(id));
    }

    @Override
    public List<Pharmacy> findAllByDistrict(Long district) {
        return pharmacyRepository.findAllByDistrict(district);
    }

    @Override
    public List<Pharmacy> findAllByDistrictAndChain(Long district, Long pharmacyChain) {
        return pharmacyRepository.findAllByDistrictAndChain(district, pharmacyChain);
    }

    @Autowired
    public void setPharmacyRepository(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }
}
