package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.reference.spring_server.entitis.District;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.repositories.MedicineRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("medicineService")
public class MedicineServiceImpl implements MedicineService {

    private MedicineRepository medicineRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findByPharmacyId(long id) {
        return medicineRepository.findByPharmacyId(id);
    }

    @Override
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public List<Medicine> findByName(String name) {
        return medicineRepository.findByName(name);
    }

    @Override
    @Transactional
    public List<Medicine> saveAll(List<Medicine> medicines) {
        return medicineRepository.saveAll(medicines);
    }

    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medicine> findAllByPage(Pageable pageable) {
        return medicineRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findByNameAndDistrict(String name, String district) {
        return medicineRepository.findByNameAndDistrict(name, district);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findByPharmacyId(Long id) {
        return medicineRepository.findByPharmacyId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findByNameAndPharmacy(String name, Long id) {
        return medicineRepository.findByNameAndPharmacy(name, id);
    }



    @Override
    public void deleteByPharmacyId(long pharmacyId) {
        medicineRepository.deleteAll(medicineRepository.findByPharmacyId(pharmacyId));
    }

    @Override
    public void deleteById(long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public Integer countByPharmacy(long id) {
        return medicineRepository.countByPharmacy(id);
    }

    @Autowired
    public void setMedicineRepository(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }
}
