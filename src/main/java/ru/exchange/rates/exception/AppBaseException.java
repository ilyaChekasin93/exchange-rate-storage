package ru.exchange.rates.exception;

import lombok.Getter;

import static ru.exchange.rates.utils.DateUtils.getCurrentDate;

@Getter
public class AppBaseException extends RuntimeException {

    protected String time;

    public AppBaseException(String message){
        super(message);
        this.time = getCurrentDate();
    }
}
