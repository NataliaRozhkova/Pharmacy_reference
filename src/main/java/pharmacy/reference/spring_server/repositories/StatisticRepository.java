package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entitis.Statistic;

import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT b from Statistic b  JOIN FETCH b.pharmacy  WHERE b.pharmacy.pharmacyId = :id")
    List<Statistic> findByPharmacyId(@Param("id") Long id);

}
