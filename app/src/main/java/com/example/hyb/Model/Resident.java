package com.example.hyb.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Resident {
    private String address;
    private String city;
    private String country;
    private String host;
    private ArrayList<String> occupants = new ArrayList<>();

    public Resident(String address, String city, String country, String host) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.host = host;
    }

    public Resident() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void addOccupants(String userID) {
        occupants.add(userID);
    }

    @Override
    public String toString() {
        return "Resident{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}