package pharmacy.reference.server.hendlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.entity.Pharmacy;
import pharmacy.reference.data.repository.Repository;

import java.io.IOException;

public class GetPharmacyHandler extends BaseHandler<Long, Pharmacy> implements HttpHandler {

    private final Repository repository;

    public GetPharmacyHandler(Repository repository) {
        this.repository = repository;
    }

    @Override
    Long handleGetRequest(HttpExchange httpExchangeParameters) throws IOException {
        String[] httpExchange = httpExchangeParameters.getRequestURI().getQuery().split("=");
        return Long.parseLong(httpExchange[1]);
    }

    @Override
    Long handlePostRequest(HttpExchange httpExchangeParameters) throws IOException {
        return null;
    }

    @Override
    Response<Pharmacy> requestRepository(Long requestParameter) throws IOException {
        return repository.readPharmacy(requestParameter);
    }

    @Override
    String presentResponse(Response<Pharmacy> response) {
        if (response.state == Response.State.ERROR) {
            return "Pharmacy not found";
        }
        return new Gson().toJson(response.body);
    }
}
