package pharmacy.reference.spring_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.entitis.PharmacyChain;
import pharmacy.reference.spring_server.parser.PharmacyParser;
import pharmacy.reference.spring_server.repositories.MedicineRepository;
import pharmacy.reference.spring_server.repositories.PharmacyChainRepository;
import pharmacy.reference.spring_server.repositories.PharmacyRepository;
import pharmacy.reference.spring_server.repositories.StatisticRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private final MedicineRepository medicineRepository;

    @Autowired
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    private final StatisticRepository statisticRepository;

    @Autowired
    private final PharmacyChainRepository chainRepository;

    public PharmacyController(MedicineRepository medicineRepository,
                              PharmacyRepository pharmacyRepository,
                              StatisticRepository statisticRepository,
                              PharmacyChainRepository chainRepository) {
        this.medicineRepository = medicineRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.statisticRepository = statisticRepository;
        this.chainRepository = chainRepository;
    }

    @ModelAttribute("pharmacies")
    public void populatePharmacy(Model model) {
        model.addAttribute("pharmacies",pharmacyRepository.findAll());
    }

    @GetMapping("/one")
    @ResponseBody
    public Pharmacy getPharmacy(@RequestParam(name = "id", required = false, defaultValue = "") Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id).get();
        return pharmacy;
    }

    @GetMapping("")
    @ResponseBody
    public List<Pharmacy> getAll() {
        return pharmacyRepository.findAll();
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

    @GetMapping("/parse")
    @ResponseBody
    public List<Pharmacy> parsePharmacy() {

        try {
            pharmacyRepository.saveAll(new PharmacyParser(chainRepository.findAll()).parse(new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/main/resources/" +
                    "Pharmacy_list.xlsx")));
        } catch (IOException e) {
            System.out.println("!!!!!!");
        }

        return pharmacyRepository.findAll();
    }

    @GetMapping("/chains")
    @ResponseBody
    public List<PharmacyChain> getPharmacyChains() {
        return chainRepository.findAll();
    }


}
