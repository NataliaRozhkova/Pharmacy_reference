package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pharmacy.reference.spring_server.entitis.District;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Town;
import pharmacy.reference.spring_server.services.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/auxiliary")
public class AuxiliaryController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    private MedicineService medicineService;
    private PharmacyService pharmacyService;
    private PharmacyChainService chainService;
    private TownService townService;
    private DistrictService districtService;
    private StatisticService statisticService;


    @GetMapping("/add/town")
    public String showAddFormTown(Town town) {
        return "town_add";
    }

    @PostMapping("/add/town")
    public String checkShowAddFormTown(@Valid Town town, Model model) {
        model.addAttribute("text", townService.save(town).getName());
        logger.info("Добавлен город" + town + ": Оператор " + SecurityContextHolder.getContext().getAuthentication().getName());
        return "base_page";
    }

    @GetMapping("/add/district")
    public String showAddFormDistrict(District district) {
        return "district_add";
    }

    @PostMapping("/add/district")
    public String checkShowAddFormDistrict(@Valid District district, Model model) {
        model.addAttribute("text", districtService.save(district).getName());
        logger.info("Добавлен район" + district + ": Оператор " + SecurityContextHolder.getContext().getAuthentication().getName());
        return "base_page";
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
    public void setChainService(PharmacyChainService chainService) {
        this.chainService = chainService;
    }

    @Autowired
    public void setTownService(TownService townService) {
        this.townService = townService;
    }

    @Autowired
    public void setDistrictService(DistrictService districtService) {
        this.districtService = districtService;
    }

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
