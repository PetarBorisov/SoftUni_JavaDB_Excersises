package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {
    private static final String COUNTRY_FILE_PATH = "src/main/resources/files/json/countries.json";

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
@Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRY_FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        List<CountrySeedDto> countries = Arrays.stream(
                this.gson.fromJson(readCountriesFromFile(),CountrySeedDto[].class))
                .collect(Collectors.toList());


        for (CountrySeedDto country : countries) {
            stringBuilder.append(System.lineSeparator());

            if (this.countryRepository.findByCountryName(country.getCountryName()).isPresent() ||
                    !this.validationUtil.isValid(country)) {
                stringBuilder.append(String.format("Invalid country"));
                continue;
            }
            this.countryRepository.save(this.modelMapper.map(country, Country.class));
            stringBuilder.append(String.format("Successfully imported country %s - %s",country.getCountryName(),
                    country.getCurrency()));
        }
        return stringBuilder.toString().trim();
    }
}
