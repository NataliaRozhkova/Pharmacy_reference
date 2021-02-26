package pharmacy.reference.spring_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entitis.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
}
