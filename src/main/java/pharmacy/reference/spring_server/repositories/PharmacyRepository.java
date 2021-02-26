package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entitis.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
