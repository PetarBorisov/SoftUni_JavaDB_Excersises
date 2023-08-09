package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDto;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private static final String CITY_FILE_PATH = "src/main/resources/files/json/cities.json";

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
@Autowired
    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITY_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        List<CitySeedDto> cities = Arrays.stream(
                gson.fromJson(readCitiesFileContent(), CitySeedDto[].class))
                        .collect(Collectors.toList());

        for (CitySeedDto city : cities) {
            stringBuilder.append(System.lineSeparator());

            if (this.cityRepository.findByCityName(city.getCityName()).isPresent() ||
                    !this.validationUtil.isValid(city)) {
                stringBuilder.append(String.format("Invalid city"));
                continue;
            }
            this.cityRepository.save(this.modelMapper.map(city, City.class));
            stringBuilder.append(String.format("Successfully imported city %s - %d",
                    city.getCityName(),city.getPopulation()));
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public City findCityById(Long city) {
        return null;
    }
}
