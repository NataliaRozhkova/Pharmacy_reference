package pharmacy.reference.data.entity;


import java.util.List;

public class Pharmacy {
    private long pharmacyId;
    private String name;
    private String telephoneNumbers;
    private String address;
    private String district;
    private List<Medicine> medicines;

    public Pharmacy(long pharmacyId,
                    String name,
                    String telephoneNumbers,
                    String address,
                    String district) {
        this.pharmacyId = pharmacyId;
        this.name = name;
        this.telephoneNumbers = telephoneNumbers;
        this.address = address;
        this.district = district;
    }

    public Pharmacy(long pharmacyId,
                    String name,
                    String telephoneNumbers,
                    String address,
                    String district,
                    List<Medicine> medicines) {
        this.pharmacyId = pharmacyId;
        this.name = name;
        this.telephoneNumbers = telephoneNumbers;
        this.address = address;
        this.district = district;
        this.medicines = medicines;
    }

    public Pharmacy() {
    }

    public long getPharmacyId() {
        return pharmacyId;
    }

    public String getName() {
        return name;
    }

    public String getTelephoneNumbers() {
        return telephoneNumbers;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        return district;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setPharmacyId(long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelephoneNumbers(String telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return (int) pharmacyId * prime
                +  telephoneNumbers.hashCode()
                + name.hashCode()
                + address.hashCode()
                + district.hashCode();
    }

    public String toString() {

        return pharmacyId + "\t" +
                name + "\t" +
                telephoneNumbers + "\t" +
                address + "\t" +
                district + "\t";
    }


}
