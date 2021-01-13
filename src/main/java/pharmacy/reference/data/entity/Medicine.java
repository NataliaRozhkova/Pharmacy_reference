package pharmacy.reference.data.entity;

public class Medicine {
    private long id;
    private String name;
    private float price;
    private int quantity;
    private String manufacturer;
    private Pharmacy pharmacy;
    private String pharmacyName;

    public Medicine(long id,
                    String name,
                    float price,
                    int quantity,
                    String manufacturer,
                    String pharmacyName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
        this.pharmacyName = pharmacyName;
    }

    public Medicine(long id,
                    String name,
                    float price,
                    int quantity,
                    String manufacturer,
                    Pharmacy pharmacy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
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
