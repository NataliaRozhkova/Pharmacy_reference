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
import pharmacy.reference.spring_server.entitis.PhoneCall;
import pharmacy.reference.spring_server.entitis.Statistic;
import pharmacy.reference.spring_server.services.*;
import pharmacy.reference.spring_server.util.MedicineGrid;

import javax.validation.Valid;
import java.util.*;
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
    private StatisticService statisticService;

    private MessageSource messageSource;

    @GetMapping(value = "")
    public String getPage(Model model, SecurityContextHolder auth) {

        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("towns", townService.findAll());
        model.addAttribute("chains", chainService.findAll());
        model.addAttribute("pharmacies", pharmacyService.findAllVisible());
        model.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getName());
        return "get_medicine_from_name";
    }

    @GetMapping("/get/all")
    @ResponseBody
    public MedicineGrid getAll(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "district", required = false) Long district,
                               @RequestParam(name = "town", required = false) Long town,
                               @RequestParam(name = "chain", required = false) Long chain,
                               @RequestParam(name = "pharmacy", required = false) Pharmacy pharmacy,
                               @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "rows", required = false) Integer rows
    ) {
        List<String> words = splitLine(name);
        List<Medicine> medicines = new ArrayList<>();
        if (pharmacy != null && pharmacy.getPharmacyId() != 0) {
            medicines = medicineService.findByNameAndPharmacy(words.get(0), pharmacy.getPharmacyId());
        } else {
            medicines = medicineService.findByName(words.get(0));
        }
        System.out.println(name);
        System.out.println(district);
        System.out.println(town);
        System.out.println(chain);
        System.out.println(page);
//        words.remove(0);
        medicines = medicines.stream()
                .filter(
                        medicine -> {
                            boolean res = true;
                            if (district != null) {
                                if (medicine.getPharmacy().getDistrict() != null) {
                                    res &= medicine.getPharmacy().getDistrict().getId() == district;
                                } else {
                                    res &= false;
                                }
                            }
                            if (town != null) {
                                if (medicine.getPharmacy().getTown() != null) {
                                    res &= medicine.getPharmacy().getTown().getId() == town;
                                }else {
                                    res &= false;
                                }
                            }
                            if (chain != null) {
                                res &= medicine.getPharmacy().getPharmacyChain().getId() == chain;
                            }
                            if (words.size() > 0) {
                                for (String word : words) {
                                    res &= medicine.getName().toLowerCase(Locale.ROOT).contains(word.toLowerCase(Locale.ROOT));
                                }
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
        } else {
            medicineGrid.setCurrentPage(page);
            medicineGrid.setTotalPages(medicines.size() / rows + 1);
            medicineGrid.setMedicines(medicines.subList(page - 1, page - 1 + rows));
        }

        return medicineGrid;

    }

    private List<String> splitLine(String line) {
        return Arrays.asList(line.split(" ").clone());
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

    @GetMapping("/get/from/pharmacy/and/medicine")
    public String getFromPharmacyAndMedicine(Model model,
                                             @RequestParam(name = "medicine_name", required = false, defaultValue = "") String name,
                                             @RequestParam(name = "pharmacyId", required = false, defaultValue = "") Long pharmacy_id) {
        model.addAttribute("pharmacy", pharmacyService.findById(pharmacy_id));
        model.addAttribute("medicines", medicineService.findByNameAndPharmacy(name, pharmacy_id));
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

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        medicineService.deleteById(id);
        return "Лекарство удалено";
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

    @GetMapping("/defecture/put")
    @ResponseBody
    public Medicine putDefecture(@RequestParam(name = "medicines") String medicinesName,
                                 @RequestParam(name = "role") String role) {
        Medicine medicineDefecture = new Medicine();
        medicineDefecture.setName(medicinesName);
        medicineDefecture.setPharmacy(pharmacyService.findByName("деф").get(0));
        medicineDefecture.setDate(new Date(System.currentTimeMillis()));
        PhoneCall phoneCall = new PhoneCall();
        phoneCall.setDate(new Date(System.currentTimeMillis()));
        medicineService.save(medicineDefecture);
        Statistic statistic = new Statistic();
        statistic.setMedicineName(medicinesName);
        statistic.setPharmacy(pharmacyService.findByName("деф").get(0));
        statistic.setPhoneCall(phoneCall);
        statistic.setOperator(role);
        statisticService.save(statistic);
        return medicineDefecture;
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

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
}

