package pharmacy.reference.spring_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entity.Medicine;
import java.util.List;

@Controller
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private final MedicineRepository medicineRepository;

    @Autowired
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    private final StatisticRepository statisticRepository;

    public MedicineController(MedicineRepository medicineRepository, PharmacyRepository pharmacyRepository, StatisticRepository statisticRepository) {
        this.medicineRepository = medicineRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/get_all")
    @ResponseBody
    public List<Medicine> getAll(@RequestParam(name = "name", required = false, defaultValue = "") String name) {
        List<Medicine> medicines = medicineRepository.findByName(name);
        System.out.println(medicines.size());
        return medicines;
    }

    @GetMapping("/get_one")
    @ResponseBody
    public Medicine getOne(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
        Medicine medicine = medicineRepository.findById(id).get();
        System.out.println(medicine.toString());
        return medicine;
    }


    @GetMapping("/delete")
    public void delete(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
//        statisticRepository.
        medicineRepository.deleteById(id);
    }



}

