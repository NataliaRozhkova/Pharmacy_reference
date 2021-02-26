package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entitis.Town;

public interface TownRepository extends JpaRepository<Town, Long> {
}
