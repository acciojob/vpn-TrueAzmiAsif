package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    private String originalIp;
    private String maskedIp;
    private boolean connected;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Connection> connectionList=new ArrayList<>();
    @ManyToMany(mappedBy = "userList",cascade = CascadeType.ALL)
    List<ServiceProvider> ServiceProviderList;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    Country country;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<ServiceProvider> getServiceProviderList() {
        return ServiceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        ServiceProviderList = serviceProviderList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOriginalIp() {
        return originalIp;
    }

    public void setOriginalIp(String originalIp) {
        this.originalIp = originalIp;
    }

    public String getMaskedIp() {
        return maskedIp;
    }

    public void setMaskedIp(String maskedIp) {
        this.maskedIp = maskedIp;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
