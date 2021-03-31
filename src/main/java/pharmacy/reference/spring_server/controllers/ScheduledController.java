package pharmacy.reference.spring_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.parser.MedicineParser;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.PharmacyService;
import pharmacy.reference.spring_server.util.EmailDownloader;
import pharmacy.reference.spring_server.util.ZipExtractor;
import pharmacy.reference.spring_server.web.YAMLConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/email")
public class ScheduledController {

    @Autowired
    private YAMLConfig config;
    private MedicineService medicineService;
    private PharmacyService pharmacyService;
    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);


    @GetMapping(value = "/download")
    @ResponseBody
    @Scheduled(cron = "0 25 14 * * *")
    public void autoUpdate() throws IOException {
        LocalDate localDate = new Date(System.currentTimeMillis() - 86400000l * 49).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        downloadEmail(localDate);
        updateMedicines(localDate);

    }


    public String downloadEmail(LocalDate localDate) throws IOException {

        String date = localDate.getDayOfMonth() + "-" + localDate.getMonthValue() + "-" + localDate.getYear();
        Path downloadPath = Path.of(config.getEmailDownloadPath() + "/" + date);
        if (!Files.exists(downloadPath)) {
            Files.createDirectory(downloadPath);
        }
        EmailDownloader emailDownloader = new EmailDownloader(config.getEmailAddress(), config.getPassword(), downloadPath.toString(), logger);
        try {
            emailDownloader.connect();
            emailDownloader.openFolder("INBOX");
            emailDownloader.downloadFiles(localDate);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return "OK";
    }

    //    @GetMapping(value = "/download")
//    @ResponseBody
    public void updateMedicines(LocalDate localDate) {
        String date = localDate.getDayOfMonth() + "-" + localDate.getMonthValue() + "-" + localDate.getYear();

        List<File> files = listFilesForFolder(new File(config.getEmailDownloadPath() + "/" + date));
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            String[] directories = file.getAbsolutePath().split("/");
            String email = directories[directories.length - 1];
            List<Pharmacy> pharmacies = pharmacyService.findAllByEmail(email.toLowerCase(Locale.ROOT));
            if (pharmacies.size() == 1) {
                parseFile(pharmacies.get(0), file, localDate);
                logger.info(pharmacies.get(0) + " Данные обновлены " + localDate);
                file.delete();
            } else {
                List<File> folders = listFilesForFolder(file);
                for (File medicineFile : folders) {
                    String addressDirectory = medicineFile.getAbsolutePath().split("/")[medicineFile.getAbsolutePath().split("/").length - 1];
                    Pharmacy pharmacy = findPharmacyFromAddress(pharmacies, addressDirectory);
                    if (pharmacy != null) {
                        parseFile(pharmacy, medicineFile, localDate);
                    } else {
                        List<File> archives = listFilesForFolder(medicineFile);
                        for (File fl : archives) {
                            if (fl.getAbsolutePath().toLowerCase().contains("zip")) {
                                ZipExtractor.unzip(fl.getAbsolutePath());
                                List<File> fileList = listFilesForFolder(new File(fl.getAbsolutePath().replaceAll("\\.zip", "")));
                                for (File currentFile : fileList) {
                                    String address = currentFile.getAbsolutePath().split("/")[currentFile.getAbsolutePath().split("/").length - 1];
                                    Pharmacy pharmacyByFind = findPharmacyFromAddress(pharmacies, address);
                                    if (pharmacyByFind != null) {
                                        parseFile(pharmacyByFind, currentFile, localDate);
                                    }
                                }
                            }
                        }



                    }

                }
            }

        }
    }

    private void parseFile(Pharmacy pharmacy, File file, LocalDate localDate) {
        File medicineFile;
        if (file.isDirectory()) {
            medicineFile = file.listFiles()[0];
            if (medicineFile.isDirectory()) {
                medicineFile = medicineFile.listFiles()[0];
            }
        } else {
            medicineFile = file;
        }
        MedicineParser parser = new MedicineParser(pharmacy);
        List<Medicine> medicines = parser.parse(medicineFile);
        if (medicines.size() > 5) {
            medicineService.saveAll(medicines);
            pharmacy.setLastUpdateMedicines(new Date(System.currentTimeMillis()));
            pharmacyService.save(pharmacy);
            logger.info(pharmacy + " Данные обновлены " + localDate);

        }
    }


    private Pharmacy findPharmacyFromAddress(List<Pharmacy> pharmacies, String address) {
        address = address.replaceAll(",", " ").replaceAll("\\."," ").replaceAll("ул\\.", " ").toLowerCase(Locale.ROOT);
        for (Pharmacy pharmacy : pharmacies) {
            List<String> pharmacyAddress = Arrays.asList(pharmacy.getAddress()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(",", "")
                    .replaceAll("ул\\.", "")
                    .replaceAll("пр\\.","")
                    .replaceAll("\\.", " ")
                    .split(" ").clone());
            boolean isAddress = true;
            for (String word : pharmacyAddress) {
                if (address.contains(word)) {
                    isAddress &= true;
                } else {
                    isAddress &= false;
                }
            }
            if (isAddress) {
                return pharmacy;
            }
        }
        return null;
    }

    private List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {

            files.add(fileEntry);
        }
        return files;
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
