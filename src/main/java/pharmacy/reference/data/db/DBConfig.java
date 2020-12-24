package pharmacy.reference.data.db;

import com.google.gson.annotations.SerializedName;

import java.io.FileWriter;
import java.io.IOException;

public class DBConfig {

    private static final String HIBERNATE_CONFIG_PATH = "src/main/resources/hibernate.cfg.xml";
    private static String HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<!DOCTYPE hibernate-configuration SYSTEM \"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\n" +
            "<hibernate-configuration>\n" +
            "    <session-factory>\n" +
            "        <property name=\"hibernate.connection.driver_class\">org.postgresql.Driver</property>\n" +
            "        <property name=\"hibernate.connection.url\">jdbc:postgresql://localhost:5432/";
    private static String BODY_1 = "</property>\n" +
            "        <property name=\"hibernate.dialect\">org.hibernate.dialect.PostgreSQL10Dialect</property>\n" +
            "        <property name=\"hibernate.connection.username\">";
    private static String BODY_2 = "</property>\n" +
            "        <property name=\"connection.password\">";
    private static String BODY_3 = "</property>\n" +
            "        <property name=\"hibernate.hbm2ddl.auto\">update</property>\n" +
            "        <mapping resource=\"Medicine.hbm.xml\"/>\n" +
            "        <mapping resource=\"Pharmacy.hbm.xml\"/>\n" +
            "    </session-factory>\n" +
            "</hibernate-configuration>";
    @SerializedName("database_name")
    public String dbName;
    @SerializedName("user_name")
    public String userName;
    public String password;

    public DBConfig(String dbName, String userName, String password) {
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    public void setHibernateConfigFile() {
        try {
            FileWriter writer = new FileWriter(HIBERNATE_CONFIG_PATH);
            writer.write(HEAD);
            writer.write(dbName);
            writer.write(BODY_1);
            writer.write(userName);
            writer.write(BODY_2);
            writer.write(password);
            writer.write(BODY_3);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
