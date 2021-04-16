package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.entitis.PharmacyChain;
import pharmacy.reference.spring_server.parser.PharmacyParser;
import pharmacy.reference.spring_server.services.*;
import pharmacy.reference.spring_server.web.YAMLConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    private PharmacyService pharmacyService;
    private PharmacyChainService pharmacyChainService;
    private MedicineService medicineService;
    private DistrictService districtService;
    private TownService townService;
    private StatisticService statisticService;
    @Autowired
    private YAMLConfig config;


    @GetMapping("pharmacies")
    public String getAll(Model model) {
        List<Pharmacy> pharmacies = pharmacyService.findAll();
        HashMap<Long, Integer> countMedicines = new HashMap<>();
        for (Pharmacy pharmacy : pharmacies) {
            Integer size = medicineService.countByPharmacy(pharmacy.getPharmacyId());
            countMedicines.put(pharmacy.getPharmacyId(), size);
            if (size > 0) {
            }
        }
        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("countMedicines", countMedicines);
        return "pharmacy_list";
    }

    @GetMapping("/{id}")
//    @ResponseBody
    public String getPharmacy(@PathVariable("id") Long id, Model model) {
        Pharmacy pharmacy = pharmacyService.findById(id);
        model.addAttribute("pharmacies", new ArrayList<Pharmacy>().add(pharmacy));
        return "pharmacy_list";
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String deletePharmacy(@PathVariable("id") Long id, Model model) {
        medicineService.deleteByPharmacyId(id);
        statisticService.deleteFromPharmacyId(id);
        pharmacyService.delete(id);
        return "Аптека удалена";
    }


    @GetMapping("/new")
    public String addPharmacy(Model model) {
        Pharmacy pharmacy = new Pharmacy();
        model.addAttribute("pharmacy", pharmacy);
        model.addAttribute("chains", pharmacyChainService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("towns", townService.findAll());


        return "pharmacy_add";
    }

    @GetMapping("/update/{id}")
    public String updatePharmacy(@PathVariable("id") Long id,
                                 Model model) {
        Pharmacy pharmacy = pharmacyService.findById(id);
        model.addAttribute("pharmacy", pharmacy);
        model.addAttribute("chains", pharmacyChainService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("towns", townService.findAll());
        return "pharmacy_update";
    }

    @PostMapping("/save")
    public String savePharmacy(Pharmacy pharmacy, Model model) {
        logger.info("Pharmacy data changed " + pharmacy + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());
        pharmacyService.save(pharmacy);
        model.addAttribute("text", pharmacy);
        return "base_page";
    }

    @GetMapping("/parse")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    public String parse() throws IOException {
        PharmacyParser pharmacyParser = new PharmacyParser(pharmacyChainService.findAll(), districtService.findAll(), townService.findAll());
        List<Pharmacy> pharmacies = pharmacyParser.parse(new File("/workspace/BOOT-INF/classes/Pharmacy_list.xlsx"));
        pharmacyService.saveAll(pharmacies);
        logger.info("Parsing pharmacy data from a file " + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());

        return "pharmacy_update";
    }

    @GetMapping(value = "/add/file/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addAll(@PathVariable("id") Long id, Model model) {
        Pharmacy pharmacy = pharmacyService.findById(id);
        List<Pharmacy> pharmacies = new ArrayList<>();
        pharmacies.add(pharmacy);
        model.addAttribute("pharmacies", pharmacies);

        return "file_loader";
    }

    @GetMapping("/chains")
    @ResponseBody
    public List<PharmacyChain> getPharmacyChains() {
        return pharmacyChainService.findAll();
    }

    @Autowired
    public void setPharmacyService(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @Autowired
    public void setPharmacyChainService(PharmacyChainService pharmacyChainService) {
        this.pharmacyChainService = pharmacyChainService;
    }

    @Autowired
    public void setDistrictService(DistrictService districtService) {
        this.districtService = districtService;
    }

    @Autowired
    public void setTownService(TownService townService) {
        this.townService = townService;
    }

    @Autowired
    public void setMedicineService(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}
