package pharmacy.reference.spring_server.entitis;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "statistic", schema = "public")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date")
    private Date date;
    @Column(name = "medicine_name")
    private String medicineName;
    @Column(name = "medicine_price")
    private float medicinePrice;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pharmacy_id", referencedColumnName = "pharmacy_id")
    private Pharmacy pharmacy;
    @ManyToOne
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private Operator operator;

    public Statistic() {
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Statistic))
            return false;

        Statistic statistic = (Statistic) o;
        return Objects.equals(this.id, statistic.id) &&
                Objects.equals(this.date, statistic.date) &&
                Objects.equals(this.pharmacy, statistic.pharmacy) &&
                Objects.equals(this.operator, statistic.operator) &&
                Objects.equals(this.medicineName, statistic.medicineName) &&
                Objects.equals(this.medicinePrice, statistic.medicinePrice)

                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.date, this.pharmacy, this.operator);
    }

    public String toString() {
        return id + "\t" +
                date + "\t" +
                medicineName + "\t" +
                medicinePrice + "\t" +
                pharmacy.getName() + "\t" +
                operator.getId() + "\t";
    }


}