package com.driver.services.impl;

import com.driver.CountryNotFoundException;
import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin adm=new Admin();
        adm.setUserName(username);
        adm.setPassword(password);
        adminRepository1.save(adm);
        return adm;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Optional<Admin> optAdm=adminRepository1.findById(adminId);
        Admin adm=optAdm.get();
        ServiceProvider sPro=new ServiceProvider();
        sPro.setName(providerName);
        sPro.setAdmin(adm);
        adm.getServiceProviders().add(sPro);
        adminRepository1.save(adm);
        Admin response=new Admin();
        response.setPassword(adm.getPassword());
        response.setId(adm.getId());
        response.setUserName(adm.getUserName());
        response.setServiceProviders(adm.getServiceProviders());
        return response;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        Optional<ServiceProvider> optSPro=serviceProviderRepository1.findById(serviceProviderId);
        if(!countryName.equalsIgnoreCase("jpn") && !countryName.equalsIgnoreCase("chi") && !countryName.equalsIgnoreCase("usa") && !countryName.equalsIgnoreCase("aus") && !countryName.equalsIgnoreCase("ind"))
            throw new CountryNotFoundException("Country not found");
        ServiceProvider sPro= optSPro.get();
        Country count=new Country();
        count.setCountryName(CountryName.valueOf(countryName.toUpperCase()));
        count.setCode(count.getCountryName().toCode());
        count.setServiceProvider(sPro);
        sPro.getCountryList().add(count);
        serviceProviderRepository1.save(sPro);
        return sPro;
    }
}
