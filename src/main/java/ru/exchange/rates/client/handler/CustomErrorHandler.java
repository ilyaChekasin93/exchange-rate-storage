package ru.exchange.rates.client.handler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class CustomErrorHandler implements ResponseErrorHandler {

    public boolean hasError(ClientHttpResponse response) { return false; }

    public void handleError(ClientHttpResponse response) { }
}
