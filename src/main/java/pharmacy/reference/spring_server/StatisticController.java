package pharmacy.reference.spring_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entity.Statistic;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private final MedicineRepository medicineRepository;

    @Autowired
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    private final StatisticRepository statisticRepository;

    public StatisticController(MedicineRepository medicineRepository, PharmacyRepository pharmacyRepository, StatisticRepository statisticRepository) {
        this.medicineRepository = medicineRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/new")
    @ResponseBody
    public Statistic putStat(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
        Statistic statistic = new Statistic();
        statistic.setMedicine(medicineRepository.findById(id).get());
        statistic.setDate(new Date(System.currentTimeMillis()));
        return statisticRepository.save(statistic);
    }


    @GetMapping("/get_all")
    @ResponseBody
    public List<Statistic> getStat() {
        return statisticRepository.findAll();
    }

}
