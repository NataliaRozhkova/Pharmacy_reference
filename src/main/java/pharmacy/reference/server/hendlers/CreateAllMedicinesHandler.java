package pharmacy.reference.server.hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pharmacy.reference.PharmacyFileReader;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.entity.parser.MedicineParser;
import pharmacy.reference.data.repository.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CreateAllMedicinesHandler extends BaseHandler<String, String> implements HttpHandler {

    private static final String DOWNLOAD_FILE_PATH = "src/main/resources/";
    private final String CREATE_ALL_MEDICINES_HTML_PAGE_PATH = "src/main/resources/medicines_add_all.html";

    private final Repository repository;

    public CreateAllMedicinesHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String requestParamValue;
        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
            handleResponse(httpExchange, requestParamValue);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(httpExchange);
            handleResponse(httpExchange, presentResponse(requestRepository(requestParamValue)));
        }
    }


    @Override
    String handleGetRequest(HttpExchange httpExchangeParameters) throws IOException {
        return PharmacyFileReader.getFile(CREATE_ALL_MEDICINES_HTML_PAGE_PATH);
    }

    @Override
    String handlePostRequest(HttpExchange httpExchangeParameters) throws IOException {
        InputStream stream = httpExchangeParameters.getRequestBody();
        String file = new String(stream.readAllBytes());
        stream.close();
        System.out.println(file);
        String filePath = DOWNLOAD_FILE_PATH  + getFileName(file);
        FileWriter writer = new FileWriter(filePath);
        writer.write(file);
        writer.flush();
        writer.close();
        return filePath;
    }

    private String getFileName (String file) {
        String fileName = new String();
        fileName = file.split("filename=\"")[1].split("\"")[0];
        System.out.println(fileName);
        return fileName;
    }

    @Override
    Response<String> requestRepository(String requestParameter) throws IOException {
        MedicineParser parser = new MedicineParser(repository.readPharmacy(1).body);
        List<Medicine> medicines = parser.parse(new File(requestParameter));
        return repository.createAllMedicines(medicines);
    }

    @Override
    String presentResponse(Response<String> response) {
        return response.body;
    }
}
