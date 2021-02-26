package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pharmacy.reference.spring_server.entitis.*;
import pharmacy.reference.spring_server.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    public String getPage(Model model) {

        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("towns", townService.findAll());
        model.addAttribute("chains", chainService.findAll());
        model.addAttribute("pharmacies", pharmacyService.findAll());

        return "get_medicine_from_name";
    }

    @GetMapping("/get/all")
    @ResponseBody
    public List<Medicine> getAll(@RequestParam(name = "name", required = false, defaultValue = "") String name,
                                 @RequestParam(name = "district", required = false, defaultValue = "") String district,
                                 @RequestParam(name = "town", required = false, defaultValue = "") String town,
                                 @RequestParam(name = "chain", required = false, defaultValue = "") Long chain,
                                 @RequestParam(name = "pharmacy", required = false, defaultValue = "") Pharmacy pharmacy) {
        List<Medicine> medicines = new ArrayList<>();
        if (pharmacy != null && pharmacy.getPharmacyId() != 0) {
            medicines = medicineService.findByNameAndPharmacy(name, pharmacy.getPharmacyId());
        } else {
            medicines = medicineService.findByName(name);
        }
        return medicines.stream()
                .filter(
                        medicine -> {
                            boolean res = true;
                            if (district != null) {
                                res &= medicine.getPharmacy().getDistrict().contains(district);
                            }
                            if (town != null) {
                                res &= medicine.getPharmacy().getTown().contains(town);
                            }
                            if (chain != null) {
                                res &= medicine.getPharmacy().getPharmacyChain().getId() == chain;
                            }

                            return res;
                        }
                ).collect(Collectors.toList());

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
    @ResponseBody
    public List<Medicine> getAllFromPharmacyId(@RequestParam(name = "pharmacy", required = false, defaultValue = "") Pharmacy pharmacy) {
        if (pharmacy == null) {
            return null;
        } else return medicineService.findByPharmacyId(pharmacy.getPharmacyId());
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
//        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
//        model.addAttribute("pharmacies", pharmacies);
        return "medicine_add_one";
    }

    @PostMapping("/add")
    public String checkShowAddForm(@Valid Medicine medicine, BindingResult bindingResult, Model model) {

//        if (bindingResult.hasErrors()) {
//            List<Pharmacy> pharmacies = pharmacyRepository.findAll();
//            model.addAttribute("pharmacies", pharmacies);
//            return "medicine_add_one";
//        }

        medicineService.save(medicine);
        return medicineService.findByName(medicine.getName()).toString();
    }


//    @PostMapping("/add")
//    public

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

