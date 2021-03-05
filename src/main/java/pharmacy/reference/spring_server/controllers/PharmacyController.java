package pharmacy.reference.spring_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.entitis.PharmacyChain;
import pharmacy.reference.spring_server.parser.PharmacyParser;
import pharmacy.reference.spring_server.services.DistrictService;
import pharmacy.reference.spring_server.services.PharmacyChainService;
import pharmacy.reference.spring_server.services.PharmacyService;
import pharmacy.reference.spring_server.services.TownService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    private PharmacyService pharmacyService;

    private PharmacyChainService pharmacyChainService;

    private DistrictService districtService;

    private TownService townService;

    @GetMapping("pharmacies")
    public String getAll(Model model) {
        model.addAttribute("pharmacies", pharmacyService.findAll());
        return "pharmacy_list";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getPharmacy(@PathVariable("id") Long id, Model model) {
        Pharmacy pharmacy = pharmacyService.findById(id);
        model.addAttribute("pharmacies", pharmacy);
        return "pharmacy_list";
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
//                                 @RequestParam(name = "chain", required = false, defaultValue = "") Long chain,
                                 Model model) {
        Pharmacy pharmacy = pharmacyService.findById(id);
        model.addAttribute("pharmacy", pharmacy);
        model.addAttribute("chains", pharmacyChainService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("towns", townService.findAll());
//        model.addAttribute("chain_select", pharmacy.getPharmacyChain());
        return "pharmacy_update";
    }

    @PostMapping("/save")
    public String savePharmacy(Pharmacy pharmacy,
                               @RequestParam(name = "chain", required = false, defaultValue = "") Long chain,
                               Model model) {
//        pharmacy.setPharmacyChain(pharmacyChainService.findById(chain));
        System.out.println(chain);
        pharmacyService.save(pharmacy);
        model.addAttribute("text", pharmacy);
        return "base_page";
    }

    @GetMapping("/parse")
    @ResponseBody
    public String parse() throws IOException {
        PharmacyParser pharmacyParser = new PharmacyParser(pharmacyChainService.findAll(), districtService.findAll(), townService.findAll());
        List<Pharmacy> pharmacies = pharmacyParser.parse(new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/main/resources/Pharmacy_list.xlsx"));
        pharmacyService.saveAll(pharmacies);

        return "pharmacy_update";
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
}
