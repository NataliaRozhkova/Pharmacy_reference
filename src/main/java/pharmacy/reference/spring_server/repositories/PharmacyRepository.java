package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT b FROM Pharmacy b WHERE lower(b.name)  LIKE  concat('%', lower(:name), '%')")
    List<Pharmacy> findByName(@Param("name") String name);

    @Query("SELECT b FROM Pharmacy b WHERE b.visibility = 'true'")
    List<Pharmacy> findAllVisible();

    @Query("SELECT b FROM Pharmacy b WHERE b.pharmacyChain.id = :chainId")
    List<Pharmacy> findAllByPharmacyChain(@Param("chainId") Long chainId);

    @Query("SELECT b FROM Pharmacy b WHERE b.email = :email")
    List<Pharmacy> findAllByEmail(@Param("email") String email);
}
