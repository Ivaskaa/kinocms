package com.example.kinocms.service;

import com.example.kinocms.entities.City;
import com.example.kinocms.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    @Autowired
    CityRepository cityRepository;

    public Page<City> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return cityRepository.findAll(pageable);
    }
    public void saveCity(City cityForm) {
        City city = new City();
        city.setName(cityForm.getName());
        cityRepository.save(city);
    }
    public void editCity(City cityForm, Long id) {
        City city = cityRepository.findById(id).orElseThrow(); // orElseThrow викидає виключення якшо запис був не найдений
        city.setName(cityForm.getName());
        cityRepository.save(city);
    }
}
