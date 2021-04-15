package pharmacy.reference.spring_server.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pharmacy.reference.spring_server.entitis.Medicine;

import java.util.List;

public interface MedicineService {

    List<Medicine> findAll();

    List<Medicine> findByPharmacyId(long id);

    Medicine save(Medicine medicine);

    List<Medicine> findByName(String name);

    List<Medicine> saveAll(List<Medicine> medicines);

    Medicine findById(Long id);

    Page<Medicine> findAllByPage(Pageable pageable);

    List<Medicine> findByNameAndDistrict(String name, String district);

    List<Medicine> findByPharmacyId(Long id);

    List<Medicine> findByNameAndPharmacy(String name, Long id);

    void deleteByPharmacyId(long pharmacyId);

    void deleteById(long id);

    Integer countByPharmacy(long id);

}
