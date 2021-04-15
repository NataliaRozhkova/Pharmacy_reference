package pharmacy.reference.spring_server.controllers;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pharmacy.reference.spring_server.entitis.*;
import pharmacy.reference.spring_server.entitis.reports.*;
import pharmacy.reference.spring_server.services.*;
import pharmacy.reference.spring_server.util.MedicineGrid;
import pharmacy.reference.spring_server.util.ZipExtractor;
import pharmacy.reference.spring_server.web.YAMLConfig;
import pharmacy.reference.spring_server.writer.ExcelWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    private YAMLConfig config;

    private MedicineService medicineService;

    private PharmacyService pharmacyService;

    private StatisticService statisticService;

    private PharmacyChainService pharmacyChainService;

    private PhoneCallService phoneCallService;

    private final Logger logger = LoggerFactory.getLogger(MedicineController.class);


    @GetMapping("/put")
    @ResponseBody
    public Integer putStat(@RequestParam(name = "medicines") List<Long> medicinesId,
                           @RequestParam(name = "role") String role) {
        List<Statistic> statistics = new ArrayList<>();
        PhoneCall phoneCall = new PhoneCall();
        phoneCall.setDate(new Date(System.currentTimeMillis()));
        phoneCallService.save(phoneCall);
        for (long id : medicinesId) {
            Statistic statistic = new Statistic();
            Medicine medicine = medicineService.findById(id);
            statistic.setMedicineName(medicine.getName());
            statistic.setMedicinePrice(medicine.getPrice());
            statistic.setPharmacy(medicine.getPharmacy());
            statistic.setPhoneCall(phoneCall);
            statistic.setOperator(role);
            statistics.add(statistic);
        }
        return statisticService.saveAll(statistics).size();
    }

    @GetMapping(value = "/view")
    @ResponseBody
    public MedicineGrid test(@RequestParam(name = "medicines") List<Long> medicinesId) {
        List<Medicine> medicines = new ArrayList<>();
        MedicineGrid medicineGrid = new MedicineGrid();

        if (!medicinesId.isEmpty()) {
            for (Long id : medicinesId) {
                medicines.add(medicineService.findById(id));
            }
            medicineGrid.setTotalRecords(medicines.size());
            medicineGrid.setTotalPages(1);
            medicineGrid.setCurrentPage(1);
            medicineGrid.setMedicines(medicines);
        }
        return medicineGrid;
    }

    @GetMapping("/file")
    public String createStat() {
        return "statistic_file_create";
    }

    @GetMapping("/file/download")
    public String downloadFilePage(Model model) {
        model.addAttribute("files", listFilesForFolder(new File(config.getStatisticPath())).stream()
                .map(file -> file.getName())
                .sorted()
                .collect(toList()));

        return "download_statistic_file";
    }

    @RequestMapping(value = "/file/download/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadFile(@PathVariable("fileName") String fileName,  HttpServletResponse response) throws IOException {
        File file = new File(config.getStatisticPath() + "/" + fileName);
        response.setHeader("Content-Type", "application/zip");
        response.getOutputStream().write(new FileInputStream(file).readAllBytes());


    }

    @GetMapping("/file/create")
    public String createStat(Model model,
                             @RequestParam(name = "month", required = false, defaultValue = "") String period
    ) throws IOException, ParseException {
        logger.debug(period);
        int month = Integer.parseInt(period.split("-")[1]);
        int year = Integer.parseInt(period.split("-")[0]);
        generateFile(year, month, 1);
        model.addAttribute("text", "Файлы статистики сгенерированы");
        logger.info("Сгененированы файлы статистики за " + month + "-" + year);
        return "base_page";
    }

    // * "0 0 * * * *" = the top of every hour of every day.
    //* "*/10 * * * * *" = every ten seconds.
    //* "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    //* "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    //* "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    //* "0 0 0 25 12 ?" = every Christmas Day at midnight

    @Scheduled(cron = "0 0 7 1 * *")
    public void autoCreateStatAllFiles() throws IOException, ParseException {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Calendar.getInstance().get(Calendar.DATE);
        logger.info("Автоматичсески сгененированы файлы статистики за " + month + "-" + year);
        generateFile(year, month, day);

    }

    private void generateFile(int year, int month, int day) throws IOException, ParseException {

        Path folder = Paths.get(config.getStatisticPath());
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        }
        String filePath = config.getStatisticPath() + "/" + month + "-" + year;

        createStatFile(year, month, day, filePath);

        createWorkStatFile(year, month, day, filePath);

        ZipExtractor.writeToZipFile(listFilesForFolder(new File(filePath)), filePath + ".zip");

        FileUtils.deleteDirectory(new File(filePath));
        medicineService.deleteByPharmacyId(pharmacyService.findByName("Дефектура").get(0).getPharmacyId());
    }

    private static List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {

            files.add(fileEntry);
        }
        return files;
    }

    private void createStatFile(int year, int month, int day, String path) throws ParseException, IOException {

        Date startDate = setFirstDate(year, month, day);
        Date finishDate = setLastDate(year, month, day);

        List<PharmacyChain> chains = pharmacyChainService.findAll();
        for (PharmacyChain chain : chains) {
            if (!chain.getName().equals("Розничные аптеки")) {

                Path filePath = Paths.get(path);
                if (!Files.exists(filePath)) {
                    Files.createDirectory(filePath);

                }
                ExcelWriter writer = new ExcelWriter(filePath + "/" + chain.getName(),
                        month,
                        year
                );
                writer.addPharmacyChainReport(createPharmacyChainReport(pharmacyService.findAllByPharmacyChain(chain.getId()), startDate, finishDate));

                writer.addCallsReport(
                        pharmacyService.findAllByPharmacyChain(chain.getId())
                                .stream()
                                .flatMap(pharmacy ->
                                        statisticService
                                                .findByPharmacyIdAndDate(pharmacy.getPharmacyId(), startDate, finishDate)
                                                .stream()
                                )
                                .map(statistic ->
                                        new CallsReport(
                                                statistic.getPhoneCall().getDate(),
                                                statistic.getMedicineName(),
                                                statistic.getPharmacy().getName() + "(" + statistic.getPharmacy().getAddress() + ")",
                                                statistic.getMedicinePrice()
                                        )

                                )
                                .sorted(new CallsReportComparator())
                                .collect(toList())

                );

                writer.write();

            } else {
                for (Pharmacy pharmacy : pharmacyService.findAllByPharmacyChain(chain.getId())) {
                    String pharmacyNameAdress = pharmacy.getName() + "(" + pharmacy.getAddress() + ")";
                    Path filePath = Paths.get(path);
                    if (!Files.exists(filePath)) {
                        Files.createDirectory(filePath);
                    }
                    ExcelWriter writer = new ExcelWriter(filePath + "/" + pharmacyNameAdress,
                            month,
                            year
                    );
                    writer.addPharmacyReport(new PharmacyReport(pharmacyNameAdress, pharmacyStatisticCallCount(pharmacy, startDate, finishDate),
                            pharmacyStatisticSumPrice(pharmacy)));
                    writer.addCallsReport(
                            statisticService.findByPharmacyIdAndDate(pharmacy.getPharmacyId(), startDate, finishDate)
                                    .stream()
                                    .map(statistic ->
                                            new CallsReport(
                                                    statistic.getPhoneCall().getDate(),
                                                    statistic.getMedicineName(),
                                                    statistic.getPharmacy().getName() + "(" + statistic.getPharmacy().getAddress() + ")",
                                                    statistic.getMedicinePrice()
                                            )

                                    )
                                    .sorted(new CallsReportComparator())
                                    .collect(toList())
                    );
                    writer.write();
                }
            }
        }
    }

    private void createWorkStatFile(int year, int month, int day, String path) throws IOException, ParseException {
        Date startDate = setFirstDate(year, month, day);
        Date finishDate = setLastDate(year, month, day);

        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            Files.createDirectory(filePath);
        }

        ExcelWriter writer = new ExcelWriter(filePath + "/" + "work_statistic", month, year);

        writer.addOverallPharmacyChainReport(
                new OverallPharmacyChainReport(
                        pharmacyChainService.findAll()
                                .stream()
                                .map(pharmacyChain ->
                                        createPharmacyChainReport(pharmacyService.findAllByPharmacyChain(pharmacyChain.getId()), startDate, finishDate)
                                )
                                .collect(toList())
                )
        );

        writer.addCallsReportWithPrice(
                pharmacyService.findAll()
                        .stream()
                        .flatMap(pharmacy ->
                                statisticService
                                        .findByPharmacyIdAndDate(pharmacy.getPharmacyId(), startDate, finishDate)
                                        .stream()
                        )
                        .map(statistic ->
                                new CallsReport(
                                        statistic.getPhoneCall().getDate(),
                                        statistic.getMedicineName(),
                                        statistic.getPharmacy().getName() + "(" + statistic.getPharmacy().getAddress() + ")",
                                        statistic.getMedicinePrice()
                                )

                        )
                        .sorted(new CallsReportComparator())
                        .collect(toList())
        );

        writer.addDefectureReports(
                statisticService.findByPharmacyIdAndDate(
                        pharmacyService.findByName("Дефектура").get(0).getPharmacyId(), startDate, finishDate
                )
                        .stream()
                        .map(statistic -> statistic.getMedicineName())
                        .collect(toList())
        );

        writer.addCallCountReports(new OverallCallsCountReport(countCallsFromDay(startDate, finishDate)));
        writer.write();
    }

    private PharmacyChainReport createPharmacyChainReport(List<Pharmacy> pharmacies, Date startDate, Date finishDate) {
        List<PharmacyReport> reports = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacies) {
            reports.add(new PharmacyReport(pharmacy.getName() + "(" + pharmacy.getAddress() + ")",
                    pharmacyStatisticCallCount(pharmacy, startDate, finishDate),
                    pharmacyStatisticSumPrice(pharmacy)
            ));
        }
        return new PharmacyChainReport(reports);
    }

    private int pharmacyStatisticCallCount(Pharmacy pharmacy, Date startDate, Date finishDate) {
        Set<PhoneCall> calls = new HashSet<>();
        for (Statistic statiistic : statisticService.findByPharmacyIdAndDate(pharmacy.getPharmacyId(), startDate, finishDate)) {
            calls.add(statiistic.getPhoneCall());
        }
        return calls.size();
    }

    private float pharmacyStatisticSumPrice(Pharmacy pharmacy) {
        return statisticService.findByPharmacyId(pharmacy.getPharmacyId())
                .stream()
                .map(statistic -> statistic.getMedicinePrice())
                .reduce(0f, (a, b) -> a + b);
    }



    private List<CallsCountReport> countCallsFromDay(Date startDate, Date finishDate) {
        List<CallsCountReport> reports = new ArrayList<>();
        LocalDate currentDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate lastDate = finishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (int i = currentDate.getDayOfMonth(); i <= lastDate.getDayOfMonth(); i++) {
            Date dateBegin = Date.from(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), i).atStartOfDay().atZone(ZoneId.systemDefault())
                    .toInstant());
            Date dateFinish = Date.from(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), i).atTime(23, 59, 59).atZone(ZoneId.systemDefault())
                    .toInstant());

            reports.add(new CallsCountReport(dateBegin,
                    phoneCallService.findByPeriod(dateBegin, dateFinish).size())
            );

        }
        return reports;
    }


    @GetMapping("/get_all")
    @ResponseBody
    public List<Statistic> getStat() {
        return statisticService.findAll();
    }


    private Date setFirstDate(int year, int month, int day) throws ParseException {
        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate start = localDate.withDayOfMonth(1);

        return Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private Date setLastDate(int year, int month, int day) throws ParseException {
        LocalDate localDate = LocalDate.of(year, month, day);
        LocalDate finish = localDate.withDayOfMonth(localDate.lengthOfMonth());
        return Date.from(finish.atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant());
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
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Autowired
    public void setPharmacyChainService(PharmacyChainService pharmacyChainService) {
        this.pharmacyChainService = pharmacyChainService;
    }

    @Autowired
    public void setPhoneCallService(PhoneCallService phoneCallService) {
        this.phoneCallService = phoneCallService;
    }

    @Autowired
    public void setConfig(YAMLConfig config) {
        this.config = config;
    }
}
