package pharmacy.reference.spring_server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entity.Medicine;

import java.util.List;

interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("select b from Medicine b where lower(b.name)  LIKE  concat('%', lower(:name), '%')")
    List<Medicine> findByName(@Param("name") String name);

    @Query("SELECT b from Medicine b  JOIN FETCH b.pharmacy  where lower(b.name)  LIKE  concat('%', lower(:name), '%')" +
            " AND lower(b.pharmacy.district)  LIKE  concat('%', lower(:district), '%')")
    List<Medicine> findByNameAndDistrict(@Param("name") String name, @Param("district") String district);



}