package pharmacy.reference.spring_server;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacy.reference.spring_server.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {


}
