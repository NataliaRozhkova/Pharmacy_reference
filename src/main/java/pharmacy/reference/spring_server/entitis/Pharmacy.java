package pharmacy.reference.spring_server.entitis;


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
    @ManyToOne
    @JoinColumn(name = "pharmacy_chain", referencedColumnName = "id")
    private PharmacyChain pharmacyChain;
    @Column
    private String telephoneNumbers;
    @Column
    private String address;
    @ManyToOne
    @JoinColumn(name = "town", referencedColumnName = "id")
    private Town town;
    @ManyToOne
    @JoinColumn(name = "district", referencedColumnName = "id")
    private District district;
    @Column
    private String email;
    @Column
    private boolean visibility;

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

    public PharmacyChain getPharmacyChain() {
        return pharmacyChain;
    }

    public void setPharmacyChain(PharmacyChain pharmacyChain) {
        this.pharmacyChain = pharmacyChain;
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

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String toString() {

        return pharmacyId + "\t" +
                name + "\t" +
                pharmacyChain.getName() + "\t" +
                telephoneNumbers + "\t" +
                address + "\t" +
                town.getName() + "\t" +
                district.getName() + "\t" +
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
                Objects.equals(this.pharmacyChain, pharmacy.pharmacyChain) &&
                Objects.equals(this.address, pharmacy.address) &&
                Objects.equals(this.telephoneNumbers, pharmacy.telephoneNumbers) &&
                Objects.equals(this.town, pharmacy.town) &&
                Objects.equals(this.district, pharmacy.district) &&
                Objects.equals(this.email, pharmacy.email) &&
                Objects.equals(this.visibility, pharmacy.visibility)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pharmacyId,
                this.name,
                this.pharmacyChain,
                this.town,
                this.address,
                this.telephoneNumbers,
                this.district,
                this.email,
                this.visibility);

    }
}