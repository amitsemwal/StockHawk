package com.sam_chordas.android.stockhawk;

import com.squareup.otto.Bus;

public class QuoteBuilder {
    private double high;
    private double low;
    private double close;
    private long volume;
    private String date;
    private String symbol;


    public QuoteBuilder setHigh(double high) {
        this.high = high;
        return this;
    }

    public QuoteBuilder setLow(double low) {
        this.low = low;
        return this;
    }

    public QuoteBuilder setClose(double close) {
        this.close = close;
        return this;
    }

    public QuoteBuilder setVolume(long volume) {
        this.volume = volume;
        return this;
    }

    public QuoteBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    public QuoteBuilder setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Quote createQuote() {
        return new Quote(high, low, close, volume, date, symbol);
    }
}