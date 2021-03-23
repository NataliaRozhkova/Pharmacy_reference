package pharmacy.reference.spring_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pharmacy.reference.spring_server.entitis.*;
import pharmacy.reference.spring_server.entitis.reports.*;
import pharmacy.reference.spring_server.services.*;
import pharmacy.reference.spring_server.util.MedicineGrid;
import pharmacy.reference.spring_server.writer.ExcelWriter;

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


    private MedicineService medicineService;

    private PharmacyService pharmacyService;

    private StatisticService statisticService;

    private PharmacyChainService pharmacyChainService;

    private PhoneCallService phoneCallService;

    private final String STATISTIC_PATH = "/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/main/resources/statistics";


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
                System.out.println(id);
                medicines.add(medicineService.findById(id));
            }
            medicineGrid.setTotalRecords(medicines.size());
            medicineGrid.setTotalPages(1);
            medicineGrid.setCurrentPage(1);
            medicineGrid.setMedicines(medicines);
        }
        return medicineGrid;
    }

    @GetMapping("/create/statistic/file")
    @ResponseBody
    public String createStat() throws IOException, ParseException {
        createStatAllFiles();
        createStatWorkFile();
        return "Ok";
    }

    @Scheduled(cron = "0 45 14 * * *")
    private void createStatAllFiles() throws IOException, ParseException {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Calendar.getInstance().get(Calendar.DATE);

        Date startDate = setFirstDate(year, month, day);
        Date finishDate = setLastDate(year, month, day);

        List<PharmacyChain> chains = pharmacyChainService.findAll();
        for (PharmacyChain chain : chains) {
            if (!chain.getName().equals("Розничные аптеки")) {
                Path filePath = Paths.get(STATISTIC_PATH + "/" + chain.getName());
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
                    Path filePath = Paths.get(STATISTIC_PATH + "/" + pharmacyNameAdress);
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

    // * "0 0 * * * *" = the top of every hour of every day.
    //* "*/10 * * * * *" = every ten seconds.
    //* "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    //* "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    //* "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    //* "0 0 0 25 12 ?" = every Christmas Day at midnight

    @Scheduled(cron = "0 46 14 * * *")
    private void createStatWorkFile() throws IOException, ParseException {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Calendar.getInstance().get(Calendar.DATE);
        Date startDate = setFirstDate(year, month, day);
        Date finishDate = setLastDate(year, month, day);

        Path filePath = Paths.get(STATISTIC_PATH + "/" + "work_statistic");
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

//    @GetMapping("/get/month/{id}")
//    @ResponseBody
//    public List<Statistic> getStatMonth(@PathVariable("id") Long id) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//        Date start = formatter.parse("2021-03-01");
//        Date finish = formatter.parse("2021-03-31");
//        return statisticService.findByPharmacyIdAndDate(id, start, finish);
//    }

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
}
