package ru.exchange.rates.scheduler;

import org.springframework.stereotype.Component;
import ru.exchange.rates.service.ExchangeRatesService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Component
@EnableScheduling
public class ScheduledTasks {

    private ExchangeRatesService exchangeRatesService;

    public ScheduledTasks(ExchangeRatesService exchangeRatesService){
        this.exchangeRatesService = exchangeRatesService;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void saveRatesFromAllSources() {
        exchangeRatesService.saveRatesFromAllSources();
    }

}
