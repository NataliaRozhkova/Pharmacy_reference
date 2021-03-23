package pharmacy.reference.spring_server.entitis.reports;

public class PharmacyReport {

    public final String name;

    public final int callCount;

    public final float sumPrice;

    public PharmacyReport(String name, int callCount, float sumPrice) {
        this.name = name;
        this.callCount = callCount;
        this.sumPrice = sumPrice;
    }
}
