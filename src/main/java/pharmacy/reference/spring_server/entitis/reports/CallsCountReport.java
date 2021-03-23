package pharmacy.reference.spring_server.entitis.reports;

import java.util.Date;

public class CallsCountReport {
    public final Date date;

    public final int callCount;

    public CallsCountReport(Date date, int callCount) {
        this.date = date;
        this.callCount = callCount;
    }
}
