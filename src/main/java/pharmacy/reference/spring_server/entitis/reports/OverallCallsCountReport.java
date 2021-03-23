package pharmacy.reference.spring_server.entitis.reports;

import java.util.List;

public class OverallCallsCountReport {

    public final List<CallsCountReport> callsCountReports;

    public final Overall overall;

    public OverallCallsCountReport(List<CallsCountReport> callsCountReports) {
        this.callsCountReports = callsCountReports;
        this.overall = new Overall(countCalls(callsCountReports), 0f);
    }

    private int countCalls(List<CallsCountReport> callsCountReports){
        return callsCountReports.stream()
                .mapToInt(callsCountReport -> callsCountReport.callCount)
                .sum();
    }
}
