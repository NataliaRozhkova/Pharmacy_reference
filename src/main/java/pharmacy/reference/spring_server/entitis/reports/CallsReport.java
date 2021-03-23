package pharmacy.reference.spring_server.entitis.reports;

import javax.xml.crypto.Data;
import java.util.Date;

public class CallsReport {

    public final Date date;

    public final String medicinesName;

    public final String pharmacyName;

    public final float price;


    public CallsReport(Date date, String medicinesName, String pharmacyName, float price) {
        this.date = date;
        this.medicinesName = medicinesName;
        this.pharmacyName = pharmacyName;
        this.price = price;
    }
}
