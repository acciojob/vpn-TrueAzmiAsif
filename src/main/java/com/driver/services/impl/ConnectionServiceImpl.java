package com.driver.services.impl;

import com.driver.AlreadyConnectedException;
import com.driver.AlreadyDisconnectedException;
import com.driver.CannotEstablishCommunicationException;
import com.driver.UnableToConnectException;
import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception {
        /** Helloo */
        Optional<User> optUser = userRepository2.findById(userId);
        User user = optUser.get();
        List<ServiceProvider> listPro = new ArrayList<>();
        if (user.isConnected()) throw new AlreadyConnectedException("Already connected");
        else if (user.getCountry().getCountryName().toString().equalsIgnoreCase(countryName)) return user;
        else {
            if (user.getServiceProviderList().isEmpty()) throw new UnableToConnectException("Unable to connect");
            else {
                for (ServiceProvider x : user.getServiceProviderList()) {
                    for (Country y : x.getCountryList()) {
                        if (y.getCountryName().toString().equalsIgnoreCase(countryName)) {
                            listPro.add(x);
                        }
                    }
                }
                if (listPro.isEmpty())
                    throw new UnableToConnectException("Unable to connect");
                else {
                    int min = Integer.MAX_VALUE;
                    for (ServiceProvider x : listPro) {
                        if (x.getId() < min) min = x.getId();
                    }
                    Connection con=new Connection();
                    user.setMaskedIp(CountryName.valueOf(countryName.toUpperCase()).toCode() + "." + min + "." + userId);
                    user.setConnected(true);
                    con.setUser(user);
                    ServiceProvider spro=serviceProviderRepository2.findById(min).get();
                    con.setServiceProvider(spro);
                    spro.getConnectionList().add(con);
                    user.getConnectionList().add(con);
                    userRepository2.save(user);
                    return user;
                }
            }
        }
    }

    @Override
    public User disconnect(int userId) throws Exception {
        Optional<User> optUser = userRepository2.findById(userId);
        User user = optUser.get();
        if (!user.isConnected()) throw new AlreadyDisconnectedException("Already disconnected");
        user.setConnected(false);
        user.setMaskedIp(null);
        userRepository2.save(user);
        return user;
    }

    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        List<ServiceProvider> listPro = new ArrayList<>();
        Optional<User> optSender = userRepository2.findById(senderId);
        User sender = optSender.get();
        Optional<User> optReceiver = userRepository2.findById(receiverId);
        User receiver = optReceiver.get();
        if (receiver.isConnected()) {
            String arr[] = receiver.getMaskedIp().split(".");
            CountryName recCount = CountryName.valueOf(arr[0]);
            if (sender.getCountry().getCountryName().toString().equals(recCount.toString())) {
                return sender;
            } else {
                for (ServiceProvider x : sender.getServiceProviderList()) {
                    for (Country y : x.getCountryList()) {
                        if (y.getCountryName().toString().equalsIgnoreCase(recCount.toString())) {
                            listPro.add(x);
                        }
                    }
                }
                if (listPro.isEmpty())
                    throw new CannotEstablishCommunicationException("Cannot establish communication");
                else {
                    int min = Integer.MAX_VALUE;
                    for (ServiceProvider x : listPro) {
                        if (x.getId() < min) min = x.getId();
                    }
                    sender.setMaskedIp(CountryName.valueOf(recCount.toString()).toCode() + "." + min + "." + senderId);
                    sender.setConnected(true);
                    return sender;
                }
            }
        } else {
            if (sender.getCountry().getCountryName().toString().equals(receiver.getCountry().getCountryName().toString())) {
                return sender;
            } else {
                for (ServiceProvider x : sender.getServiceProviderList()) {
                    for (Country y : x.getCountryList()) {
                        if (y.getCountryName().toString().equalsIgnoreCase(receiver.getCountry().getCountryName().toString())) {
                            listPro.add(x);
                        }
                    }
                }
                if (listPro.isEmpty())
                    throw new CannotEstablishCommunicationException("Cannot establish communication");
                else {
                    int min = Integer.MAX_VALUE;
                    for (ServiceProvider x : listPro) {
                        if (x.getId() < min) min = x.getId();
                    }
                    sender.setMaskedIp(CountryName.valueOf(receiver.getCountry().getCountryName().toString()).toCode() + "." + min + "." + senderId);
                    sender.setConnected(true);
                    return sender;
                }
            }

        }
        //return null;
    }
}