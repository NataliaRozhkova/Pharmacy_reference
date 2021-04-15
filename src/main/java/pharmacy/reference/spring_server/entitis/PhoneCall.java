package pharmacy.reference.spring_server.entitis;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "phone_calls", schema = "public")
public class PhoneCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date")
    private Date date;

    public PhoneCall() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
