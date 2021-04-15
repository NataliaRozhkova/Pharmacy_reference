package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.reference.spring_server.entitis.PharmacyChain;
import pharmacy.reference.spring_server.repositories.PharmacyChainRepository;

import java.util.List;

@Service("pharmacyChainService")
public class PharmacyChainServiceImpl implements PharmacyChainService {

    private PharmacyChainRepository pharmacyChainRepository;

    @Override
    public PharmacyChain findById(Long id) {
        return pharmacyChainRepository.findById(id).get();
    }

    @Override
    public PharmacyChain save(PharmacyChain pharmacyChain) {
        return pharmacyChainRepository.save(pharmacyChain);
    }

    @Override
    public List<PharmacyChain> findAll() {
        return pharmacyChainRepository.findAll();
    }

    @Override
    public void delete(PharmacyChain chain) {
        pharmacyChainRepository.delete(chain);
    }


    @Autowired
    public void setPharmacyChainRepository(PharmacyChainRepository pharmacyChainRepository) {
        this.pharmacyChainRepository = pharmacyChainRepository;
    }
}
