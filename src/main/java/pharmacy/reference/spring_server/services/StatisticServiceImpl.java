package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.reference.spring_server.entitis.Statistic;
import pharmacy.reference.spring_server.repositories.StatisticRepository;

import java.util.Date;
import java.util.List;

@Transactional
@Service("statisticService")
public class StatisticServiceImpl implements StatisticService {

    private StatisticRepository statisticRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Statistic> findAll() {
        return statisticRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Statistic> findByPharmacyId(long id) {
        return statisticRepository.findByPharmacyId(id);
    }

    @Override
    public Statistic save(Statistic statistic) {
        return statisticRepository.save(statistic);
    }

    @Override
    public List<Statistic> saveAll(List<Statistic> statistics) {
        return statisticRepository.saveAll(statistics);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Statistic> findAllByPage(Pageable pageable) {
        return statisticRepository.findAll(pageable);
    }

    @Override
    public List<Statistic> findByPharmacyIdAndDate(Long id, Date startDate, Date finishDate) {
        return statisticRepository.findByPharmacyIdAndDate(id, startDate, finishDate);
    }

    @Override
    public void deleteFromPharmacyId(Long pharmacyId) {
        statisticRepository.deleteAll(statisticRepository.findByPharmacyId(pharmacyId));
    }

    @Autowired
    public void setStatisticRepository(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }


}
