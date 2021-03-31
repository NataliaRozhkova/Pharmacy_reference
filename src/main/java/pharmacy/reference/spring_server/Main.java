package pharmacy.reference.spring_server;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.SpringApplication;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.parser.PharmacyParser;
import pharmacy.reference.spring_server.services.MedicineService;
import pharmacy.reference.spring_server.services.MedicineServiceImpl;
import pharmacy.reference.spring_server.util.ZipExtractor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Main {

    private static MedicineServiceImpl service = new MedicineServiceImpl();

    public  static void main(String[] args) throws IOException {

        String file = "/home/natasha/IdeaProjects/test/src/main/resources/user/aptekaklassika_bog_14v@fastmail.com/Аптека  74  плюс на Ч Хмельницкого, 14В.zip";
        ZipExtractor.unzip(file);
    }

}
