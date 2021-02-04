package pharmacy.reference.spring_server;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entity.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
