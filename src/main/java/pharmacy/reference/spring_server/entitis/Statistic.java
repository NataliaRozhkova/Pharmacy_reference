package pharmacy.reference.spring_server.entitis;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "statistic", schema = "public")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_call_id", referencedColumnName = "id")
    private PhoneCall phoneCall;
    @Column(name = "medicine_name")
    private String medicineName;
    @Column(name = "medicine_price")
    private float medicinePrice;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pharmacy_id", referencedColumnName = "pharmacy_id")
    private Pharmacy pharmacy;
    @Column(name = "operator_name")
    private String operator;

    public Statistic() {
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public long getId() {
        return id;
    }

    public void setPhoneCall(PhoneCall phoneCall) {
        this.phoneCall = phoneCall;
    }

    public PhoneCall getPhoneCall() {
        return phoneCall;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicinePrice(float medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public float getMedicinePrice() {
        return medicinePrice;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Statistic))
            return false;

        Statistic statistic = (Statistic) o;
        return Objects.equals(this.id, statistic.id) &&
                Objects.equals(this.phoneCall, statistic.phoneCall) &&
                Objects.equals(this.pharmacy, statistic.pharmacy) &&
                Objects.equals(this.operator, statistic.operator) &&
                Objects.equals(this.medicineName, statistic.medicineName) &&
                Objects.equals(this.medicinePrice, statistic.medicinePrice)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.phoneCall, this.pharmacy, this.operator);
    }

    public String toString() {
        return id + "\t" +
                phoneCall.getDate() + "\t" +
                medicineName + "\t" +
                medicinePrice + "\t" +
                pharmacy.getName() + "\t" +
                operator + "\t";
    }


}
