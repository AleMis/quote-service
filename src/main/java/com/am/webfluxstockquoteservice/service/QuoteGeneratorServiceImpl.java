package com.am.webfluxstockquoteservice.service;

import com.am.webfluxstockquoteservice.model.Quote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Service
public class QuoteGeneratorServiceImpl implements QuoteGeneratorService {

    private static final MathContext mathContext = new MathContext(2);
    private Random random = new Random();
    private List<Quote> quotes = new ArrayList<>();

    public QuoteGeneratorServiceImpl() {
        this.quotes.add(new Quote("AAPL", 160.16));
        this.quotes.add(new Quote("MSFT", 77.74));
        this.quotes.add(new Quote("GOOG", 847.24));
        this.quotes.add(new Quote("ORCL", 49.51));
        this.quotes.add(new Quote("IBM", 159.34));
        this.quotes.add(new Quote("INTC", 39.29));
        this.quotes.add(new Quote("RHT", 84.29));
        this.quotes.add(new Quote("VMW", 92.21));
    }

    @Override
    public Flux<Quote> fetchQuoteStream(Duration period) {
        return Flux.generate(() -> 0,
                (BiFunction<Integer, SynchronousSink<Quote>, Integer>) (index, sink) -> {
                    Quote updatedQuote = updateQuote(this.quotes.get(index));
                    sink.next(updatedQuote);
                    return ++index % this.quotes.size();
                })
                .zipWith(Flux.interval(period))
                .map(t -> t.getT1())
                .map(quote -> {
                    quote.setInstant(Instant.now());
                    return quote;
                })
                .log("QuoteGeneratorService");
    }

    private Quote updateQuote(Quote quote) {
        BigDecimal priceChange = quote.getPrice()
                .multiply(new BigDecimal(0.05 * this.random.nextDouble(), this.mathContext));
        return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
    }
}
