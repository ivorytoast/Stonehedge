package com.snow.stonehedge.marketdata.controller;

import com.snow.stonehedge.marketdata.model.Book;
import com.snow.stonehedge.marketdata.model.Quote;
import com.snow.stonehedge.shared.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController("/marketdata")
public class MarketDataController {

    @GetMapping("/v1/realtime/all")
    public List<Quote> getAllQuotes() {
        return new ArrayList<>(Data.QUOTES.values());
    }

    @GetMapping("/v1/realtime/{symbol}")
    @NonNull
    public Quote getSymbolQuote(@PathVariable String symbol) {
        return Data.QUOTES.getOrDefault(symbol, new Quote("", new Book()));
    }

}
