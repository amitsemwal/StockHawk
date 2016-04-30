package com.sam_chordas.android.stockhawk;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by semwa on 15-04-2016.
 */


public class Quote implements Parcelable {
    public static final Parcelable.Creator<Quote> CREATOR = new Parcelable.Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
    private String Symbol;
    private double High;
    private double Low;
    private double Close;
    private long Volume;
    private String Date;

    public Quote(double high, double low, double close, long volume, String date, String symbol) {
        High = high;
        Low = low;
        Close = close;
        Volume = volume;
        Date = date;
        Symbol = symbol;
    }

    protected Quote(Parcel in) {
        this.Symbol = in.readString();
        this.High = in.readDouble();
        this.Low = in.readDouble();
        this.Close = in.readDouble();
        this.Volume = in.readLong();
        this.Date = in.readString();
    }

    public String getSymbol() {
        return Symbol;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "Symbol='" + Symbol + '\'' +
                ", High=" + High +
                ", Low=" + Low +
                ", Close=" + Close +
                ", Volume=" + Volume +
                ", Date='" + Date + '\'' +
                '}';
    }

    public double getClose() {
        return Close;
    }

    public String getStrDate() {
        return Date;
    }

    public java.util.Date getDate() throws ParseException {
        return new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(Date);

    }

    public double getHigh() {
        return High;
    }

    public double getLow() {
        return Low;
    }

    public long getVolume() {
        return Volume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Symbol);
        dest.writeDouble(this.High);
        dest.writeDouble(this.Low);
        dest.writeDouble(this.Close);
        dest.writeLong(this.Volume);
        dest.writeString(this.Date);
    }
}


