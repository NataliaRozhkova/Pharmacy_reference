package pharmacy.reference.data.entity.parser;

import junit.framework.TestCase;
import org.junit.Assert;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.entity.Pharmacy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicineParserTest extends TestCase {

    private final MedicineParser parser = new MedicineParser(new Pharmacy());

    public void testParseLOFARM() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/lofarm.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseAkademApteka() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Академ аптека.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseNaSwobode() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Аптека на Свободе139.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseApteka74() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Аптека  74  плюс.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseBaikalskaya31() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Байкальская,31.xlsx");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParse6() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Ло фарм.xlsx");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseMedArtFarma() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/МедАртФарма.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseOlanceV2() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Оланс2.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParsePlanetaZdoroviya() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/планета здоровья.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseUralOnkoCentr() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/УралОнкоЦентр.xls");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseFarmimpleks() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/фармвимплекс.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseEntuziastov14b() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/Энтузиастов 14в.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseFarmservice() throws IOException {
        File file = new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/examples/chicherina30.csv");
        List<Medicine> list = parser.parse(file);
        System.out.println(file.getName() + "!  "+ list.size());
        Assert.assertNotEquals(0, list.size());
    }

    public void testParseAllFiles() {
        List<File> files = listFilesForFolder(new File("/home/natasha/IdeaProjects/Справка/pharmacy _reference/src/test/resources/examples/apteki"));
        System.out.println(files.size());
        for (File file :  files) {


            try {
                System.out.println(file.getName() + "    " + parser.parse(file).size());
            }catch (Exception e) {
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


