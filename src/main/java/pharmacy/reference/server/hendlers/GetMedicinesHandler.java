package pharmacy.reference.server.hendlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.repository.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetMedicinesHandler extends BaseHandler<HashMap<String, String>, List<Medicine>> implements HttpHandler {

    private final Repository repository;

    public GetMedicinesHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    HashMap<String, String> handleGetRequest(HttpExchange httpExchangeParameters) {
        return exchangeParametersFromRequest(httpExchangeParameters);
    }

    @Override
    HashMap<String, String> handlePostRequest(HttpExchange httpExchangeParameters) {
        return null;
    }

    @Override
    Response<List<Medicine>> requestRepository(HashMap<String, String> requestParameter) {
        return repository.readMedicineWithFilterParameter(requestParameter.get("medicine"));
    }

    @Override
    String presentResponse(Response<List<Medicine>> response) {
        return new Gson().toJson(response.body);
    }
}
