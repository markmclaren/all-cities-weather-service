package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AllCitiesWeatherController {

    @Autowired
    private CityServiceClient cityServiceClient;

    @Autowired 
    private WeatherServiceClient weatherServiceClient;

    @GetMapping("/")
    public List<Weather> getAllCitiesWeather(){
        Stream<City> allCities = cityServiceClient.getAllCities().stream().flatMap(list -> list.stream());

        //Obtain weather for all cities in parallel
        List<Weather> allCitiesWeather = allCities.parallel()
            .peek(city -> System.out.println("City: >>"+city.getName()+"<<"))
            .map(city -> weatherServiceClient.getWeatherForCity(city.getName()))
            .collect(Collectors.toList());

        return allCitiesWeather;
    }
}