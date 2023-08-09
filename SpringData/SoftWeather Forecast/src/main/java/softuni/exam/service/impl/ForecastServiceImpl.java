package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastExportDto;
import softuni.exam.models.dto.ForecastSeedRootDto;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
public class ForecastServiceImpl implements ForecastService {
    private static final String FORECAST_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";

    private final ForecastRepository forecastRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final CityRepository cityRepository;

@Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, CityRepository cityRepository) {
        this.forecastRepository = forecastRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.cityRepository = cityRepository;
}

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECAST_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(FORECAST_FILE_PATH, ForecastSeedRootDto.class)
                .getForecasts()
                .stream()
                .filter(forecast -> {
                    boolean isValid = validationUtil.isValid(forecast);
                    if (forecastRepository.findByDayOfWeekAndCity_Id(forecast.getDayOfWeek(), forecast.getCity()).isPresent()) {
                        isValid = false;
                    }
                    sb.append(isValid ? String.format("Successfully imported forecast %s - %.2f",
                            forecast.getDayOfWeek(), forecast.getMaxTemperature())
                            : "Invalid forecast").append(System.lineSeparator());
                    return isValid;

                })
                .map(forecastDto -> {
                    Forecast forecast = modelMapper.map(forecastDto, Forecast.class);
                    forecast.setCity(cityRepository.findById(forecastDto.getCity()).get());
                    return forecast;
                })
                .forEach(forecastRepository::saveAndFlush);

        return sb.toString();
    }


    @Override
    public String exportForecasts() {
        StringBuilder sb = new StringBuilder();
        Set<Forecast> exportForecast =
this.forecastRepository.findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek.SUNDAY, 150000);

        exportForecast.forEach(f -> {
            sb.append(String.format("City: %s:%n" +
                            "    -min temperature: %.2f%n" +
                            "    --max temperature: %.2f%n" +
                            "    ---sunrise: %s%n" +
                            "    -----sunset: %s%n",
                    f.getCity().getCityName(),
                    f.getMinTemperature(),
                    f.getMaxTemperature(),
                    f.getSunrise(),
                    f.getSunset()
            ));
        });
        return sb.toString();
    }
}
