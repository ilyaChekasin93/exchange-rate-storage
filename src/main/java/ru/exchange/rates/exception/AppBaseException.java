package ru.exchange.rates.exception;

import static ru.exchange.rates.utils.DateUtils.getCurrentDate;

public class AppBaseException extends RuntimeException {

    protected String time;

    public AppBaseException(String message){
        super(message);
        this.time = getCurrentDate();
    }
}
