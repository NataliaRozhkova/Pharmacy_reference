package pharmacy.reference.spring_server.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pharmacy.reference.spring_server.entitis.Statistic;

import java.util.Date;
import java.util.List;

public interface StatisticService {
    List<Statistic> findAll();

    List<Statistic> findByPharmacyId(long id);

    Statistic save(Statistic statistic);

    List<Statistic> saveAll(List<Statistic> statistics);

    Page<Statistic> findAllByPage(Pageable pageable);

    List<Statistic> findByPharmacyIdAndDate(Long id, Date startDate, Date finishDate);

}
