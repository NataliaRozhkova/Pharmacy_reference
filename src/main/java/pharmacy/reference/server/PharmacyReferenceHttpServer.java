package pharmacy.reference.server;

import com.sun.net.httpserver.HttpServer;
import pharmacy.reference.data.repository.Repository;
import pharmacy.reference.server.hendlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class PharmacyReferenceHttpServer {

    final String host;
    final int port;
    final HttpServer server;

    public PharmacyReferenceHttpServer(String host, int port, Repository repository) throws IOException {
        this.host = host;
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new StartServiceHandler());
        server.createContext("/pharmacy/read", new GetPharmacyHandler(repository));
        server.createContext("/medicine/read/filter", new GetMedicinesHandler(repository));
        server.createContext("/medicine/create/all", new CreateAllMedicinesHandler(repository));
        server.createContext("/pharmacy/create", new CreatePharmacyHandler(repository));


        server.setExecutor(Executors.newFixedThreadPool(10));
    }

    public void start() {
        server.start();
    }

    public  void  stop(int stop) {
        server.stop(stop);
    }
}
