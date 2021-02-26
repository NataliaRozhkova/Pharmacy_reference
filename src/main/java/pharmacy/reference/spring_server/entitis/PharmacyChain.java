package pharmacy.reference.spring_server.entitis;

import javax.persistence.*;

@Entity
@Table(name = "pharmacy_chain", schema = "public")
public class PharmacyChain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    public PharmacyChain() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
