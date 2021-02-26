package pharmacy.reference.spring_server.services;

import pharmacy.reference.spring_server.entitis.District;

import java.util.List;

public interface DistrictService {
    List<District> findAll();
    District findById(Long id);
    District save(District district);
}
