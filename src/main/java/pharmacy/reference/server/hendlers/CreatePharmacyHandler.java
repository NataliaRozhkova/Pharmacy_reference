package pharmacy.reference.server.hendlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.entity.Pharmacy;
import pharmacy.reference.data.repository.Repository;

import java.io.IOException;
import java.io.InputStream;

public class CreatePharmacyHandler extends BaseHandler<String, String> implements HttpHandler {

    private final Repository repository;

    public CreatePharmacyHandler(Repository repository) {
        this.repository = repository;
    }

//    @Override
//    public void handle(HttpExchange httpExchange) throws IOException {
//        T requestParamValue = null;
//        if ("GET".equals(httpExchange.getRequestMethod())) {
//            requestParamValue = handleGetRequest(httpExchange);
//        } else if ("POST".equals(httpExchange.getRequestMethod())) {
//            requestParamValue = handlePostRequest(httpExchange);
//        }
//        handleResponse(httpExchange, presentResponse(requestRepository(requestParamValue)));
//    }

    @Override
    String handleGetRequest(HttpExchange httpExchangeParameters)  {
        return null;
    }

    @Override
    String handlePostRequest(HttpExchange httpExchangeParameters) throws IOException {
        InputStream stream = httpExchangeParameters.getRequestBody();
        String json = new String(stream.readAllBytes());
        stream.close();
        return json;
    }

    @Override
    Response<String> requestRepository(String requestParameter) {
        return repository.createPharmacy(new Gson().fromJson(requestParameter, Pharmacy.class));
    }

    @Override
    String presentResponse(Response<String> response) {
        return response.body;
    }
}
