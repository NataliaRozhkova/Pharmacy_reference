package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entitis.Medicine;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("SELECT b from Medicine b WHERE lower(b.name)  LIKE  concat('%', lower(:name), '%')")
    List<Medicine> findByName(@Param("name") String name);

    @Query("SELECT b from Medicine b  JOIN FETCH b.pharmacy  WHERE lower(b.name)  LIKE  concat('%', lower(:name), '%')" +
            " AND lower(b.pharmacy.district)  LIKE  concat('%', lower(:district), '%')")
    List<Medicine> findByNameAndDistrict(@Param("name") String name, @Param("district") String district);

    @Query("SELECT b from Medicine b  JOIN FETCH b.pharmacy  WHERE b.pharmacy.pharmacyId = :id")
    List<Medicine> findByPharmacyId(@Param("id") Long id);

    @Query("SELECT b from Medicine b WHERE lower(b.name)  LIKE  concat('%', lower(:name), '%') AND b.pharmacy.pharmacyId = :id")
    List<Medicine> findByNameAndPharmacy(@Param("name") String name,@Param("id") Long id);

}