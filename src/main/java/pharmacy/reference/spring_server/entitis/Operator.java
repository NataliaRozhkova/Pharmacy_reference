package pharmacy.reference.spring_server.entitis;

import javax.persistence.*;

@Entity
@Table(name = "operators", schema = "public")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;


    public Operator() {
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
