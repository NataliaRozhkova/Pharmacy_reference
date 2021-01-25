package pharmacy.reference.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medicine {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private float price;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("country")
    private String country;
    private transient Pharmacy pharmacy;
    @SerializedName("pharmacy_name")
    private String pharmacyName;

    public Medicine(long id,
                    String name,
                    float price,
                    int quantity,
                    String manufacturer,
                    String country,
                    String pharmacyName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
        this.country = country;
        this.pharmacyName = pharmacyName;
    }

    public Medicine(long id,
                    String name,
                    float price,
                    int quantity,
                    String manufacturer,
                    String country,
                    Pharmacy pharmacy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
        this.country = country;
        this.pharmacy = pharmacy;
    }

    public Medicine() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCountry() {
        return country;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        if (pharmacy != null) {
            pharmacyName = pharmacy.getName();
        }
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }
}
