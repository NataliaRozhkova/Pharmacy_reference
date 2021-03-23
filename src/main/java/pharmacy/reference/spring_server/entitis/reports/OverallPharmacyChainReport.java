package pharmacy.reference.spring_server.entitis.reports;

import java.util.List;

public class OverallPharmacyChainReport {

    public final List<PharmacyChainReport> pharmacyChainReports;

    public final Overall overall;

    public OverallPharmacyChainReport(List<PharmacyChainReport> pharmacyChainReports) {
        this.pharmacyChainReports = pharmacyChainReports;
        this.overall = overallCount(pharmacyChainReports);
    }

    private Overall overallCount(List<PharmacyChainReport> reports) {
        int callCount = 0;
        float sumPrice = 0;
        for (PharmacyChainReport report : reports) {
            callCount += report.overall.callCount;
            sumPrice += report.overall.sumPrice;
        }
        return new Overall(callCount, sumPrice);
    }
}
