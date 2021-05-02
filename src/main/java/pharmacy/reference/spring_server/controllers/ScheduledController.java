package pharmacy.reference.spring_server.controllers;

import org.apache.commons.io.FileUtils;
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
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/email")
public class ScheduledController {

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    @Autowired
    private YAMLConfig config;
    private MedicineService medicineService;
    private PharmacyService pharmacyService;

    public static String convertCyrilic(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'ѓ', 'е', 'ж', 'з', 'ѕ', 'и', 'ј', 'к', 'л', 'љ', 'м', 'н', 'њ', 'о', 'п', 'р', 'с', 'т', 'ќ', 'у', 'ф', 'х', 'ц', 'ч', 'џ', 'ш', 'А', 'Б', 'В', 'Г', 'Д', 'Ѓ', 'Е', 'Ж', 'З', 'Ѕ', 'И', 'Ј', 'К', 'Л', 'Љ', 'М', 'Н', 'Њ', 'О', 'П', 'Р', 'С', 'Т', 'Ќ', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Џ', 'Ш', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "]", "e", "zh", "z", "y", "i", "j", "k", "l", "q", "m", "n", "w", "o", "p", "r", "s", "t", "'", "u", "f", "h", "c", ";", "x", "{", "A", "B", "V", "G", "D", "}", "E", "Zh", "Z", "Y", "I", "J", "K", "L", "Q", "M", "N", "W", "O", "P", "R", "S", "T", "KJ", "U", "F", "H", "C", ":", "X", "{", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "/", "-"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcLat.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcCyr[x]);
                }
            }
        }
        return builder.toString();
    }

    @GetMapping(value = "/download")
    @Scheduled(cron = "0 0 */2 * * *")
    @ResponseBody
    public String autoUpdate() throws IOException {
        Path folder = Paths.get(config.getEmailDownloadPath());
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        }
        LocalDate localDate = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate localDate = new Date(System.currentTimeMillis() - 86400000l * 40).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        downloadEmail(localDate);
        updateMedicines(localDate);
        return "Данные обновлены";

    }

    public String downloadEmail(LocalDate localDate) throws IOException {

        String date = localDate.getDayOfMonth() + "-" + localDate.getMonthValue() + "-" + localDate.getYear();
        Path downloadPath = Paths.get(config.getEmailDownloadPath() + "/" + date);
        if (!Files.exists(downloadPath)) {
            Files.createDirectory(downloadPath);
        }
        EmailDownloader emailDownloader = new EmailDownloader(config.getEmailAddress(), config.getEmailPassword(), downloadPath.toString(), logger);
        try {
            emailDownloader.connect();
            emailDownloader.openFolder("INBOX");
            emailDownloader.downloadFiles(localDate);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return "OK";
    }

    public void updateMedicines(LocalDate localDate) {
        String date = localDate.getDayOfMonth() + "-" + localDate.getMonthValue() + "-" + localDate.getYear();

        List<File> files = listFilesForFolder(new File(config.getEmailDownloadPath() + "/" + date));
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            String[] directories = file.getAbsolutePath().split("/");
            String email = directories[directories.length - 1];
            List<Pharmacy> pharmacies = pharmacyService.findAllByEmail(email.toLowerCase(Locale.ROOT));
            if (email.toLowerCase(Locale.ROOT).contains("svezheereshenie_gag_16@fastmail.com")) {
                System.out.println("Мы нашли его");
            }
            if (pharmacies.size() == 1) {
                parseFile(pharmacies.get(0), file, localDate);
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
        try {
            FileUtils.deleteDirectory(new File(config.getEmailDownloadPath() + "/" + date));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseFile(Pharmacy pharmacy, File file, LocalDate localDate) {
        File medicineFile;
        medicineFile = fineFileFromFolder(file);
        MedicineParser parser = new MedicineParser(pharmacy);
        List<Medicine> medicines = parser.parse(medicineFile);
        if (medicines.size() > 5) {
            medicineService.deleteByPharmacyId(pharmacy.getPharmacyId());
            medicineService.saveAll(medicines);
            pharmacy.setLastUpdateMedicines(new Date(System.currentTimeMillis()));
            pharmacyService.save(pharmacy);
            logger.info(pharmacy + " Pharmacies medicines updated " + localDate);

        } else {
            logger.info("Failed to update data for " + pharmacy + "  " + localDate);
        }
    }

    private File fineFileFromFolder(File folder) {
        if (folder.isDirectory() && listFilesForFolder(folder).size() != 0) {
            return fineFileFromFolder(listFilesForFolder(folder).get(0));

        }
        return folder;
    }

    private Pharmacy findPharmacyFromAddress(List<Pharmacy> pharmacies, String address) {



        if (!address.chars()
                .mapToObj(Character.UnicodeBlock::of)
                .anyMatch(b -> b.equals(Character.UnicodeBlock.CYRILLIC))) {
            address = convertCyrilic(address);
        }
        address = address
                .replaceAll(",", " ")
                .replaceAll("\\.", " ")
                .replaceAll("ул\\.", " ")
                .toLowerCase(Locale.ROOT);

        for (Pharmacy pharmacy : pharmacies) {
            List<String> pharmacyAddress = Arrays.asList(pharmacy.getAddress()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(",", "")
                    .replaceAll("ул\\.", "")
                    .replaceAll("пр\\.", "")
                    .replaceAll("\\.", " ")
                    .split(" ").clone());
            boolean isAddress = true;
            for (String word : pharmacyAddress) {
                if (address.contains(word)) {
                    isAddress &= true;
                } else {
                    isAddress &= false;
                    System.out.println("!!!!!!!!!!!!!!" + pharmacy);
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
