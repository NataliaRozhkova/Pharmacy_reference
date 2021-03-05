package pharmacy.reference.spring_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Statistic;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.PharmacyService;
import pharmacy.reference.spring_server.services.StatisticService;
import pharmacy.reference.spring_server.util.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    private MedicineService medicineService;

    private PharmacyService pharmacyService;

    private StatisticService statisticService;


    @GetMapping("/put")
    @ResponseBody
    public Integer putStat(@RequestParam(name = "medicines") List<Long> medicinesId,
                           @RequestParam(name = "role") String role) {
        List<Statistic> statistics = new ArrayList<>();
        for (long id : medicinesId) {
            Statistic statistic = new Statistic();
            Medicine medicine = medicineService.findById(id);
            statistic.setMedicineName(medicine.getName());
            statistic.setMedicinePrice(medicine.getPrice());
            statistic.setPharmacy(medicine.getPharmacy());
            statistic.setDate(new Date(System.currentTimeMillis()));
            statistic.setOperator(role);
            statistics.add(statistic);
        }
        return statisticService.saveAll(statistics).size();
    }

    @GetMapping(value = "/view")
    @ResponseBody
    public MedicineGrid test(@RequestParam(name = "medicines") List<Long> medicinesId) {
        List<Medicine> medicines = new ArrayList<>();
        MedicineGrid medicineGrid = new MedicineGrid();

        if (!medicinesId.isEmpty()) {
            for (Long id : medicinesId) {
                System.out.println(id);
                medicines.add(medicineService.findById(id));
            }
            medicineGrid.setTotalRecords(medicines.size());
            medicineGrid.setTotalPages(1);
            medicineGrid.setCurrentPage(1);
            medicineGrid.setMedicines(medicines);
        }
        return medicineGrid;
    }


    @GetMapping("/get_all")
    @ResponseBody
    public List<Statistic> getStat() {
        return statisticService.findAll();
    }

    @Autowired
    public void setMedicineService(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Autowired
    public void setPharmacyService(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
