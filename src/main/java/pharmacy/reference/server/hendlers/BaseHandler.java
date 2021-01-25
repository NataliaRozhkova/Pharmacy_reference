package pharmacy.reference.server.hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pharmacy.reference.data.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public abstract class BaseHandler<T, K> implements HttpHandler {

    abstract T handleGetRequest(HttpExchange httpExchangeParameters) throws IOException;

    abstract T handlePostRequest(HttpExchange httpExchangeParameters) throws IOException;

    abstract Response<K> requestRepository(T requestParameter) throws IOException;

    abstract String presentResponse(Response<K> response);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        T requestParamValue = null;
        if ("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, presentResponse(requestRepository(requestParamValue)));
    }


    public void handleResponse(HttpExchange httpExchange, String response) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public HashMap<String, String> exchangeParametersFromRequest(HttpExchange httpExchangeParameters) {
        String httpParameters = httpExchangeParameters.getRequestURI().getQuery();
        if (httpExchangeParameters != null) {
            HashMap<String, String> parameters = new HashMap<>();
            for (String parameter : httpParameters.split("&")) {
                String[] pair = parameter.split("=");
                if (pair.length > 1) {
                    parameters.put(pair[0], pair[1]);
                }
            }
            return parameters;
        } else return null;
    }


}
