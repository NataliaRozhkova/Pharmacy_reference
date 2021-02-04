package pharmacy.reference.spring_server.entity;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pharmacies", schema = "public")
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacy_id")
    private long pharmacyId;
    @Column
    private String name;
    @Column
    private String telephoneNumbers;
    @Column
    private String address;
    @Column
    private String district;
    @Column
    private String email;

    public Pharmacy() {
    }


    public long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumbers() {
        return telephoneNumbers;
    }

    public void setTelephoneNumbers(String telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String toString() {

        return pharmacyId + "\t" +
                name + "\t" +
                telephoneNumbers + "\t" +
                address + "\t" +
                district + "\t" +
                email + "\t";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Pharmacy))
            return false;

        Pharmacy pharmacy = (Pharmacy) o;
        return Objects.equals(this.pharmacyId, pharmacy.pharmacyId) &&
                Objects.equals(this.name, pharmacy.name) &&
                Objects.equals(this.address, pharmacy.address) &&
                Objects.equals(this.telephoneNumbers, pharmacy.telephoneNumbers) &&
                Objects.equals(this.district, pharmacy.district) &&
                Objects.equals(this.email, pharmacy.email)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pharmacyId,
                this.name,
                this.address,
                this.telephoneNumbers,
                this.district,
                this.email);

    }
}