package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pharmacy.reference.spring_server.entitis.District;
import pharmacy.reference.spring_server.entitis.PharmacyChain;
import pharmacy.reference.spring_server.entitis.Town;
import pharmacy.reference.spring_server.services.DistrictService;
import pharmacy.reference.spring_server.services.PharmacyChainService;
import pharmacy.reference.spring_server.services.TownService;

import javax.validation.Valid;

@Controller
@RequestMapping("/auxiliary")
public class AuxiliaryController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    private TownService townService;
    private DistrictService districtService;
    private PharmacyChainService chainService;

    @GetMapping("/list")
    public String showList(Model model) {
        model.addAttribute("towns", townService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("pharmacy_chains", chainService.findAll());
        return "auxiliary_inf_list";
    }


    @GetMapping("/add/town")
    public String showAddFormTown(Town town) {
        return "town_add";
    }

    @PostMapping("/add/town")
    public String checkShowAddFormTown(@Valid Town town, Model model) {
        model.addAttribute("text", townService.save(town).getName());
        logger.info("Add town" + town + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());
        return showList(model);
    }

    @GetMapping("/delete/town/{id}")
    public String deleteTown(@PathVariable("id") Long id, Model model) {
        townService.delete(townService.findById(id));
        return showList(model);
    }

    @GetMapping("/update/town/{id}")
    public String updateTown(@PathVariable("id") Long id,
                             Model model) {
        model.addAttribute("town", townService.findById(id));
        return "town_update";
    }

    @GetMapping("/add/district")
    public String showAddFormDistrict(District district) {
        return "district_add";
    }

    @PostMapping("/add/district")
    public String checkShowAddFormDistrict(@Valid District district, Model model) {
        model.addAttribute("text", districtService.save(district).getName());
        logger.info("Add district" + district + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());
        return showList(model);
    }

    @GetMapping("/delete/district/{id}")
    public String deleteDistrict(@PathVariable("id") Long id, Model model) {
        districtService.delete(districtService.findById(id));
        return showList(model);
    }

    @GetMapping("/update/district/{id}")
    public String updateDistrict(@PathVariable("id") Long id,
                                 Model model) {
        model.addAttribute("district", districtService.findById(id));
        return "district_update";
    }

    @GetMapping("/add/pharmacy_chain")
    public String showAddFormPharmacyChain(PharmacyChain pharmacyChain) {
        return "pharmacy_chain_add";
    }

    @PostMapping("/add/pharmacy_chain")
    public String checkShowAddFormPharmacyChain(@Valid PharmacyChain pharmacyChain, Model model) {
        model.addAttribute("text", chainService.save(pharmacyChain).getName());
        logger.info("Add pharmacy chain  " + pharmacyChain + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());
        return showList(model);
    }

    @GetMapping("/delete/pharmacy_chain/{id}")
    public String deletePharmacyChain(@PathVariable("id") Long id, Model model) {
        chainService.delete(chainService.findById(id));
        return showList(model);
    }

    @GetMapping("/update/pharmacy_chain/{id}")
    public String updatePharmacyChain(@PathVariable("id") Long id,
                                      Model model) {
        model.addAttribute("pharmacy_chain", chainService.findById(id));
        return "pharmacy_chain_update";
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

}
