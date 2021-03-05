package pharmacy.reference.spring_server.services;

import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;

import java.util.List;

public interface PharmacyService {

    Pharmacy findById(Long id);

    List<Pharmacy> findAll();

    Pharmacy save(Pharmacy pharmacy);

    List<Pharmacy> saveAll(List<Pharmacy> pharmacies);
}
