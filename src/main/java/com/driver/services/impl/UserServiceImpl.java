package com.driver.services.impl;

import com.driver.CountryNotFoundException;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        if(!countryName.equalsIgnoreCase("jpn") && !countryName.equalsIgnoreCase("chi") && !countryName.equalsIgnoreCase("usa") && !countryName.equalsIgnoreCase("aus") && !countryName.equalsIgnoreCase("ind"))
            throw new CountryNotFoundException("Country not found");
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        Country count= new Country();
        count.setCountryName(CountryName.valueOf(countryName.toUpperCase()));
        count.setCode(count.getCountryName().toCode());
        count.setUser(user);
        user.setOriginalIp(count.getCode()+"."+user.getId());
        user.setCountry(count);
        userRepository3.save(user);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        Optional<User> optUser=userRepository3.findById(userId);
        Optional<ServiceProvider> optSPro=serviceProviderRepository3.findById(serviceProviderId);
        User user= optUser.get();
        ServiceProvider sPro= optSPro.get();
        user.getServiceProviderList().add(sPro);
        sPro.getUserList().add(user);
        userRepository3.save(user);
        return user;
    }
}
