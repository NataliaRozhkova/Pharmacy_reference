package pharmacy.reference.spring_server.entitis;

import javax.persistence.*;

@Entity
@Table(name = "towns", schema = "public")
public class Town {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;

    public Town() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
