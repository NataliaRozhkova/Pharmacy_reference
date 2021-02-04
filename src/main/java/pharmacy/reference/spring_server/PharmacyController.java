package pharmacy.reference.spring_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entity.Pharmacy;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private final MedicineRepository medicineRepository;

    @Autowired
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    private final StatisticRepository statisticRepository;

    public PharmacyController(MedicineRepository medicineRepository, PharmacyRepository pharmacyRepository, StatisticRepository statisticRepository) {
        this.medicineRepository = medicineRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/one")
    @ResponseBody
    public Pharmacy getPharmacy(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id).get();
        return pharmacy;
    }

    @GetMapping("/new")
    @ResponseBody
    public Pharmacy putStat() {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName("!!!!!!!");
        pharmacy.setAddress("jfsdbvhdbhv");
        System.out.println("!!!!!!!"+pharmacy);
        return pharmacyRepository.save(pharmacy);
    }


}
