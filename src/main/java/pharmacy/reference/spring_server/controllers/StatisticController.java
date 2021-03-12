package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.entitis.PharmacyChain;
import pharmacy.reference.spring_server.entitis.Statistic;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.PharmacyChainService;
import pharmacy.reference.spring_server.services.PharmacyService;
import pharmacy.reference.spring_server.services.StatisticService;
import pharmacy.reference.spring_server.util.MedicineGrid;
import pharmacy.reference.spring_server.writer.StatisticExcelWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/statistic")
public class StatisticController {


    private MedicineService medicineService;

    private PharmacyService pharmacyService;

    private StatisticService statisticService;

    private PharmacyChainService pharmacyChainService;

    private final String STATISTIC_PATH = "/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/main/resources/statistics";


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

    @GetMapping("/create/statistic/file")
    @ResponseBody
    public String createStat() throws IOException {
        createStatAllFiles();
        return "Ok";
    }

    @Scheduled(cron = "0 0 10 * * *")
    private void createStatAllFiles() throws IOException {
        int month = Calendar.getInstance().get(Calendar.MONTH) - 1;
//        int year = Calendar.getInstance().get(Calendar.YEAR);
        int year = Calendar.getInstance().get(Calendar.MINUTE);
        System.out.println("!!!!!!!!!!");
        List<PharmacyChain> chains = pharmacyChainService.findAll();
        for (PharmacyChain chain : chains) {
            if (!chain.getName().equals("Розничные аптеки")) {
                Path filePath = Paths.get(STATISTIC_PATH + "/" + chain.getName());
                if (!Files.exists(filePath)) {
                    Files.createDirectory(filePath);
                }
                HashMap<Pharmacy, List<Statistic>> count = new HashMap<>();
                for (Pharmacy pharmacy : pharmacyService.findAllByPharmacyChain(chain.getId())) {
                    count.put(pharmacy, statisticService.findByPharmacyId(pharmacy.getPharmacyId()));
                }
                StatisticExcelWriter writer = new StatisticExcelWriter(
                        filePath.toString() + "/" + chain.getName(),
                        count, month, year);
                writer.write();
            } else {
                for (Pharmacy pharmacy : pharmacyService.findAllByPharmacyChain(chain.getId())) {
                    Path filePath = Paths.get(STATISTIC_PATH + "/" + pharmacy.getName());
                    if (!Files.exists(filePath)) {
                        Files.createDirectory(filePath);
                    }
                    HashMap<Pharmacy, List<Statistic>> count = new HashMap<>();
                    count.put(pharmacy, statisticService.findByPharmacyId(pharmacy.getPharmacyId()));
                    StatisticExcelWriter writer = new StatisticExcelWriter(
                            filePath.toString() + "/" + pharmacy.getName() + "(" + pharmacy.getAddress() + ")",
                            count,
                            month, year);
                    writer.write();
                }
            }
        }
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

    @Autowired

    public void setPharmacyChainService(PharmacyChainService pharmacyChainService) {
        this.pharmacyChainService = pharmacyChainService;
    }
}
