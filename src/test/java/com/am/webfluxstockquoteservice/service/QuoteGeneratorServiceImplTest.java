package com.am.webfluxstockquoteservice.service;

import com.am.webfluxstockquoteservice.model.Quote;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class QuoteGeneratorServiceImplTest {

    QuoteGeneratorServiceImpl quoteGeneratorService = new QuoteGeneratorServiceImpl();

    @Test
    public void fetchQuoteStream() throws Exception {

        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofSeconds(1));

        quoteFlux.take(100)
                .subscribe(System.out::println);
    }

    @Test
    public void fetchQuoteStreamCountDown() throws Exception {
        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L));

        Consumer<Quote> println = System.out::println;

        Consumer<Throwable> errorHandler = e -> System.out.println("Some Error Occurred");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable allDone = () -> countDownLatch.countDown();

        quoteFlux.take(30)
                .subscribe(println, errorHandler, allDone);

        countDownLatch.await();
    }
}
