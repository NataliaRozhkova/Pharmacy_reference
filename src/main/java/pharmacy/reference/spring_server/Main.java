package pharmacy.reference.spring_server;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.SpringApplication;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.parser.PharmacyParser;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.MedicineServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Main {

    private static MedicineServiceImpl service = new MedicineServiceImpl();

    public  static void main(String[] args) throws IOException {
    }

}
