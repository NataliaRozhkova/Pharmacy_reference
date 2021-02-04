package pharmacy.reference.spring_server.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "statistic", schema = "public")

public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column (name = "date")
    private Date date;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    private Medicine medicine;

    public Statistic() {
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
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
                Objects.equals(this.medicine, statistic.medicine)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.date, this.medicine);
    }

    public String toString() {
        return id + "\t" +
                date + "\t" +
                medicine.getName() + "\t" +
                medicine.getPharmacy().getName() + "\t" ;
    }


}
