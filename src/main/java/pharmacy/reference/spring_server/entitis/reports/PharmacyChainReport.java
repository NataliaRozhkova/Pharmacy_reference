package pharmacy.reference.spring_server.entitis.reports;

import java.util.List;

public class PharmacyChainReport {

    public final List<PharmacyReport> pharmacyReports;

    public final Overall overall;

    public PharmacyChainReport(List<PharmacyReport> pharmacyReports) {
        this.pharmacyReports = pharmacyReports;
        this.overall = overallCount(pharmacyReports);
    }


    private Overall overallCount(List<PharmacyReport> reports) {
        int callCount = 0;
        float sumPrice = 0;
        for (PharmacyReport report : reports) {
            callCount += report.callCount;
            sumPrice += report.sumPrice;
        }
        return new Overall(callCount, sumPrice);
    }
}
