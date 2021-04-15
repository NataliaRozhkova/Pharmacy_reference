package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.parser.MedicineParser;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.PharmacyService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/file")

public class FileLoaderController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    private MedicineService medicineService;
    private PharmacyService pharmacyService;

    @GetMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addAll(Model model) {
        List<Pharmacy> pharmacies = pharmacyService.findAll();
        model.addAttribute("pharmacies", pharmacies);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "file_loader";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(
//                            @RequestParam("name") String name,
            @RequestParam("pharmacy_name") Pharmacy pharmacy,
            @RequestParam("file") String name,
            Model model
    ) {

        File file = new File(name);
        try {

            List<Medicine> medicines = parseFile(file, pharmacy);
            medicineService.deleteByPharmacyId(pharmacy.getPharmacyId());
            medicineService.save(medicines.get(0));
            List<Medicine> saveMedicines = medicineService.saveAll(medicines);
            pharmacy.setLastUpdateMedicines(new Date(System.currentTimeMillis()));
            pharmacyService.save(pharmacy);
            model.addAttribute("text", "Добавлено позиций " + saveMedicines.size());
            logger.info("Updating a pharmacy drug list from a file " + pharmacy + ": Operator " + SecurityContextHolder.getContext().getAuthentication().getName());

        } catch (Exception e) {
            model.addAttribute("text", "You were unable to download  " + file.getName() + " => " + e.getMessage());
            logger.info("You were unable to download" + e.getMessage() + ": Operator  " + SecurityContextHolder.getContext().getAuthentication().getName());

        }
        return "base_page";
    }


    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String handleFileParseView(@RequestParam("name") String name,
                                      @RequestParam("pharmacy_name") Pharmacy pharmacy,
                                      @RequestParam("file") MultipartFile file,
                                      Model model) throws IOException {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String[] str = name.split("\\.");
                String newFileName = "upload_file." + str[str.length - 1];
                File newFile = new File(newFileName);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(newFile));
                stream.write(bytes);
                stream.close();

                List<Medicine> medicines = parseFile(newFile, pharmacy);

                System.out.println(newFile);

                model.addAttribute("medicines", medicines);
                model.addAttribute("file", newFile);
                model.addAttribute("pharmacy", pharmacy);
            } catch (Exception e) {
                logger.info("!!!!!!" + e.getMessage());
            }
        } else {
        }


        return "file_view";

    }

    private List<Medicine> parseFile(File file, Pharmacy pharmacy) {

        long id = pharmacy.getPharmacyId();
        return (new MedicineParser(pharmacyService.findById(id)).parse(file));


    }

    @Autowired
    public void setMedicineService(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Autowired
    public void setPharmacyService(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }
}
