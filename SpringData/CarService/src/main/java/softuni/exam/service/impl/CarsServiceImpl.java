package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Constants;
import softuni.exam.models.dto.CarsSeedDto;
import softuni.exam.models.dto.CarsSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.CarType;
import softuni.exam.repository.CarsRepository;
import softuni.exam.service.CarsService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class CarsServiceImpl implements CarsService {
    private static String CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml/";

    private final CarsRepository carRepository;
   // private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

@Autowired
    public CarsServiceImpl(CarsRepository carRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.carRepository = carRepository;
  //  this.modelMapper = modelMapper;
    this.validationUtil = validationUtil;
    this.xmlParser = xmlParser;
}

    @Override
    public boolean areImported() {
        return this.carRepository.count() >0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
    StringBuilder stringBuilder = new StringBuilder();

        final List<CarsSeedDto> cars =
                this.xmlParser
                        .fromFile(CARS_FILE_PATH,CarsSeedRootDto.class)
                        .getCars();

        for (CarsSeedDto car : cars) {
            stringBuilder.append(System.lineSeparator());

            if (this.carRepository.findFirstByPlateNumber(car.getPlateNumber()).isPresent() ||
                    !validationUtil.isValid(car)) {
                stringBuilder.append(String.format(Constants.INVALID_FORMAT, Constants.CAR));
                continue;
            }
            Car car1 = new Car();
            car1.setCarMake(car.getCarMake());
            car1.setCarModel(car.getCarModel());
            car1.setKilometers(car.getKilometers());
            car1.setPlateNumber(car.getPlateNumber());
            car1.setEngine(car.getEngine());
            car1.setYear(car.getYear());
            car1.setCarType(CarType.coupe);


        //   carRepository.save(modelMapper.map(car1,Car.class));
            carRepository.save(car1);

            stringBuilder.append(String.format(Constants.SUCCESSFUL_FORMAT, Constants.CAR,
                    car.getCarMake() + " -",
                    car.getCarModel()));
        }
        return stringBuilder.toString().trim();
    }
}
