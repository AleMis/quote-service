package com.am.webfluxstockquoteservice.model;


import lombok.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Quote {

    private static final MathContext mathContext = new MathContext(2);

    private String ticker;
    private BigDecimal price;
    private Instant instant;

    public Quote(String ticker, BigDecimal price) {
        this.ticker = ticker;
        this.price = price;
    }

    public Quote(String ticker, Double price) {
        this.ticker = ticker;
        this.price = new BigDecimal(price, mathContext);
    }

}
