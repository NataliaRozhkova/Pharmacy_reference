package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.services.*;
import pharmacy.reference.spring_server.util.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/medicine")
public class MedicineController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    private MedicineService medicineService;
    private PharmacyService pharmacyService;
    private PharmacyChainService chainService;
    private TownService townService;
    private DistrictService districtService;

    private MessageSource messageSource;

    @GetMapping(value = "")
    public String getPage(Model model, SecurityContextHolder auth) {

        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("towns", townService.findAll());
        model.addAttribute("chains", chainService.findAll());
        model.addAttribute("pharmacies", pharmacyService.findAll());
        model.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return "get_medicine_from_name";
    }

    @GetMapping("/get/all")
    @ResponseBody
    public MedicineGrid getAll(@RequestParam(name = "name", required = false, defaultValue = "") String name,
                               @RequestParam(name = "district", required = false, defaultValue = "") Long district,
                               @RequestParam(name = "town", required = false, defaultValue = "") Long town,
                               @RequestParam(name = "chain", required = false, defaultValue = "") Long chain,
                               @RequestParam(name = "pharmacy", required = false, defaultValue = "") Pharmacy pharmacy,
                               @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "rows", required = false) Integer rows
                               ) {


//        System.out.println(district);
//        System.out.println(town);
//        System.out.println(chain);
//        System.out.println(page);
//        System.out.println(rows);


        List<Medicine> medicines = new ArrayList<>();
        if (pharmacy != null && pharmacy.getPharmacyId() != 0) {
            medicines = medicineService.findByNameAndPharmacy(name, pharmacy.getPharmacyId());
        } else {
            medicines = medicineService.findByName(name);
        }
        System.out.println(medicines.size());
        medicines = medicines.stream()
                .filter(
                        medicine -> {
                            boolean res = true;
                            if (district != null) {
                                res &= medicine.getPharmacy().getDistrict().getId() == district;
                            }
                            if (town != null) {
                                res &= medicine.getPharmacy().getTown().getId() == town;
                            }
                            if (chain != null) {
                                res &= medicine.getPharmacy().getPharmacyChain().getId() == chain;
                            }

                            return res;
                        }
                ).collect(Collectors.toList());
        MedicineGrid medicineGrid = new MedicineGrid();
        medicineGrid.setTotalRecords(medicines.size());

        if (medicines.size() < rows) {
            medicineGrid.setMedicines(medicines);
            medicineGrid.setCurrentPage(1);
            medicineGrid.setTotalPages(1);
//            return medicines;
        } else {
            medicineGrid.setCurrentPage(page);
            medicineGrid.setTotalPages(medicines.size() / rows + 1);
            medicineGrid.setMedicines(medicines.subList(page - 1, page - 1 + rows));
        }

        return medicineGrid;
//        return medicines.subList(page - 1, page - 1 + rows);

    }


    @GetMapping("/get/one")
    @ResponseBody
    public Medicine getOne(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
        Medicine medicine = medicineService.findById(id);
        System.out.println(medicine);

        medicine.setName(medicine.getName() + "!!!!!!!");
        medicineService.save(medicine);
        System.out.println(medicineService.findById(id));
        return medicine;
    }

    @GetMapping("/get/from/pharmacy")
    public String getPagePharmacySelect(Model model) {
//        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        model.addAttribute("pharmacies", pharmacyService.findAll());
        return "get_medicine_from_pharmacy";
    }


    @GetMapping("/get/all/from/pharmacy")
//    @ResponseBody
    public String getAllFromPharmacyId(@RequestParam(name = "pharmacy", required = false, defaultValue = "") Pharmacy pharmacy, Model model) {
        if (pharmacy != null) {
            model.addAttribute("medicines", medicineService.findByPharmacyId(pharmacy.getPharmacyId()));
        }
        model.addAttribute("pharmacies", pharmacyService.findAll());
        model.addAttribute("pharmacy", pharmacy);
        return "get_medicine_from_pharmacy";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
//        statisticRepository.
//        try {
//            medicineRepository.deleteById(id);
//            return id + " deleted";
//        } catch (IllegalArgumentException e) {
        return "Id must be not null";
//        }
    }

    @GetMapping("/add")
    public String showAddForm(Medicine medicine, Model model) {
        model.addAttribute("pharmacies", pharmacyService.findAll());
        return "medicine_add_one";
    }

    @PostMapping("/add")
    public String checkShowAddForm(@Valid Medicine medicine, BindingResult bindingResult, Model model) {
        medicine.setDate(new Date(System.currentTimeMillis()));
        model.addAttribute("text", medicineService.save(medicine).toString());
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
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}

