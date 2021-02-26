package pharmacy.reference.spring_server;

import org.springframework.boot.SpringApplication;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.MedicineServiceImpl;

public class Main {

    private static MedicineServiceImpl service = new MedicineServiceImpl();

    public  static void main(String[] args) {
//        SpringApplication.run(Application.class, args);

    }
}
