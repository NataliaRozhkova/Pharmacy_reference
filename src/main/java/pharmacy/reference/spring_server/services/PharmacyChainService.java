package pharmacy.reference.spring_server.services;

import pharmacy.reference.spring_server.entitis.PharmacyChain;

import java.util.List;

public interface PharmacyChainService {

    PharmacyChain findById(Long id);

    PharmacyChain save(PharmacyChain pharmacyChain);

    List<PharmacyChain> findAll();
}
