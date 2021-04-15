package pharmacy.reference.spring_server.entitis.reports;

import java.util.Comparator;

public class CallsReportComparator implements Comparator<CallsReport> {
    @Override
    public int compare(CallsReport report, CallsReport t1) {
        return report.date.compareTo(t1.date);
    }
}
