package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entitis.PharmacyChain;

public interface PharmacyChainRepository extends JpaRepository<PharmacyChain, Long> {
}
