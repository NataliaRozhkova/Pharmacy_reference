package pharmacy.reference.spring_server.entitis;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "medicines", schema = "public")
public class Medicine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @Size(min=3, max=255)
    private String name;
    @Column
    private float price;
    @Column
    private int quantity;
    @Column
    private String manufacturer;
    @Column
    private String country;
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "pharmacy_id", referencedColumnName = "pharmacy_id")
    private Pharmacy pharmacy;

    public Medicine() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Medicine))
            return false;

        Medicine medicine = (Medicine) o;
        return Objects.equals(this.id, medicine.id) &&
                Objects.equals(this.name, medicine.name) &&
                Objects.equals(this.price, medicine.price) &&
                Objects.equals(this.pharmacy, medicine.pharmacy) &&
                Objects.equals(this.manufacturer, medicine.manufacturer) &&
                Objects.equals(this.quantity, medicine.quantity) &&
                Objects.equals(this.country, medicine.country) &&
                Objects.equals(this.date, medicine.date)

                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.pharmacy, this.price, this.country, this.manufacturer, this.quantity, this.date);
    }

    public String toString() {
        return id + "\t" +
                name + "\t" +
                price + "\t" +
                quantity + "\t" +
                manufacturer + "\t" +
                country + "\t" +
                pharmacy.getName() + "\t"+
                date + "\t";
    }


}


