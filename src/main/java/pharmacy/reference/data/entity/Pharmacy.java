package pharmacy.reference.data.entity;

import java.util.List;

public class Pharmacy {
    private int id;
    private String name;
    private String address;
    private String district;
    private List<Medicine> medicines;

    public Pharmacy(int id, String name, String address, String district) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.district = district;
    }

    public Pharmacy(int id, String name, String address, String district, List<Medicine> medicines) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.district = district;
        this.medicines = medicines;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
        return prime * id
                + name.hashCode()
                + address.hashCode()
                + district.hashCode();
    }

    public String toString() {
        return id + "\t" +
                name + "\t" +
                address + "\t" +
                district + "\t";
    }
}
