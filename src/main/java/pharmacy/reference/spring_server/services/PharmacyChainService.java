package pharmacy.reference.spring_server.services;

import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.PharmacyChain;

import java.util.List;

public interface PharmacyChainService {

    PharmacyChain findById(Long id);

    PharmacyChain save(PharmacyChain pharmacyChain);

    List<PharmacyChain> findAll();

}
