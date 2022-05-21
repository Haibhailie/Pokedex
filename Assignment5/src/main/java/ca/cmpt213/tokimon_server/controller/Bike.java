package ca.cmpt213.tokimon_server.controller;

import java.util.concurrent.atomic.AtomicInteger;

public class Bike {
    private static AtomicInteger nextId = new AtomicInteger();
    private int id;
    private int year;
    private String make;

    public Bike(int year, String make) {
        id = nextId.getAndIncrement();
        this.year = year;
        this.make = make;
    }

    public int getId()      { return id;   }
    public int getYear()    { return year; }
    public String getMake() { return make; }

}