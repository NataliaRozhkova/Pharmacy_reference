package pharmacy.reference.spring_server.parser;

import junit.framework.TestCase;
import org.junit.Assert;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicineParserTest extends TestCase {

    private final MedicineParser parser = new MedicineParser(new Pharmacy());

    public void testParseLOFARM() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/Розничные аптеки/Аптека Гвоздика ООО №1 пр-кт Свердловский, д111.84б111.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        System.out.println(list.get(5).getName() + "   " + list.get(5).getPrice());

        Assert.assertNotEquals(0, list.size());
    }

    public void testParseAkademApteka() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/Академ аптека.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseNaSwobode() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/Аптека на Свободе139.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseApteka74() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/Аптека  74  плюс.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseBaikalskaya31() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/Байкальская,31.xlsx");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParse6() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/Ло фарм.xlsx");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseMedArtFarma() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/МедАртФарма.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseOlanceV2() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/с вопросом/Оланс2.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        System.out.println(list.get(11).getName() + "!  " + list.get(11).getPrice());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParsePlanetaZdoroviya() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/планета здоровья.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseUralOnkoCentr() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/УралОнкоЦентр.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseFarmimpleks() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/фармвимплекс.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseEntuziastov14b() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/Энтузиастов 14в.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseFarmservice() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/с вопросом/my3.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        for (Medicine m : list) {
            System.out.println(m);
        }
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseLenina67() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/examples/apteki/Челябинск,Ленина,67.csv");
        List<Medicine> list = parser.parse(file);
        for(Medicine m : list) {
            System.out.println(m.getName());
        }
        System.out.println(file.getName() + "!  " + list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseAllFiles() {
        List<File> files = listFilesForFolder(new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/examples/apteki"));
        System.out.println(files.size());
        for (File file : files) {


            try {
                List<Medicine> medicines =  parser.parse(file);
                System.out.println(file.getName() + "    " + medicines.size() );
                System.out.println(medicines.get(11).getName() + "    " + medicines.get(11).getPrice() );            } catch (Exception e) {
                System.out.println(file.getName() + e.getMessage());
            }
        }

    }

    public void testParseHarmon() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/с вопросом/Харман Молодогвардейцев.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  " + list.size());
        for (Medicine m : list) {
            System.out.println(m.getName() + " " + m.getPrice());
        }
        Assert.assertNotEquals(0, list.size());
    }

    public void testАптека74() {
        List<File> files = listFilesForFolder(new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/аптека74"));
        System.out.println(files.size());
        for (File file : files) {


            try {
                List<Medicine> medicines =  parser.parse(file);
                System.out.println(file.getName() + "    " + medicines.size() );
                System.out.println(medicines.get(11).getName() + "    " + medicines.get(11).getPrice() );

            } catch (Exception e) {
                System.out.println(file.getName() + e.getMessage());
            }
        }

    }
    public void testPlanetHealth() {
        List<File> files = listFilesForFolder(new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/планета здоровья"));
        System.out.println(files.size());
        for (File file : files) {


            try {
                List<Medicine> medicines =  parser.parse(file);
                System.out.println(file.getName() + "    " + medicines.size() );
                System.out.println(medicines.get(11).getName() + "    " + medicines.get(11).getPrice() );
            } catch (Exception e) {
                System.out.println(file.getName() + e.getMessage());
            }
        }

    }
    public void testOther() {
        List<File> files = listFilesForFolder(new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/Розничные аптеки"));
        System.out.println(files.size());
        for (File file : files) {


            try {
                List<Medicine> medicines =  parser.parse(file);
                System.out.println(file.getName() + "    " + medicines.size() );
                System.out.println(medicines.get(11).getName() + "    " + medicines.get(11).getPrice() );
            } catch (Exception e) {
                System.out.println(file.getName() + e.getMessage());
            }
        }

    }

    public void testWhisErrors() {
        List<File> files = listFilesForFolder(new File("/home/natasha/IdeaProjects/Справка/pharmacy_reference/src/test/resources/tests/с вопросом"));
        System.out.println(files.size());
        for (File file : files) {


            try {
                List<Medicine> medicines =  parser.parse(file);
                System.out.println(file.getName() + "    " + medicines.size() );
                System.out.println(medicines.get(11).getName() + "    " + medicines.get(11).getPrice() );
            } catch (Exception e) {
                System.out.println(file.getName() + e.getMessage());
            }
        }

    }


    public List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {

            files.add(fileEntry);
        }
        return files;
    }


}


