package pharmacy.reference.server;

public class ServerConfig {

    public String host = "localhost";
    public int port = 8001;


    private ServerConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

}
