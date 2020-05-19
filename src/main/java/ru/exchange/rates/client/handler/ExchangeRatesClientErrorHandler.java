package ru.exchange.rates.client.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.exchange.rates.exception.ExchangeRateClientException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class ExchangeRatesClientErrorHandler implements ResponseErrorHandler {

    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus.Series series = httpResponse.getStatusCode().series();

        return series == CLIENT_ERROR || series == SERVER_ERROR;
    }

    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus.Series series = httpResponse.getStatusCode().series();
        String clientStatusText = httpResponse.getStatusText();

        switch (series){
            case SERVER_ERROR:
            case CLIENT_ERROR:
                throw new ExchangeRateClientException(clientStatusText);
        }
    }
}
