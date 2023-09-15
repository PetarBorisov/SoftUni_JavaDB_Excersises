package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Constants;
import softuni.exam.models.dto.MechanicSeedDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.service.MechanicsService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exam.models.Constants.*;

@Service
public class MechanicsServiceImpl implements MechanicsService {
    private static String MECHANIC_FILE_PATH = "src/main/resources/files/json/mechanics.json/";

    private final MechanicsRepository mechanicRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    @Autowired
    public MechanicsServiceImpl(MechanicsRepository mechanicRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.mechanicRepository = mechanicRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANIC_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {

        final StringBuilder stringBuilder = new StringBuilder();

        final List<MechanicSeedDto> mechanics =
                Arrays.stream(gson.fromJson(readMechanicsFromFile(), MechanicSeedDto[].class))
                        .collect(Collectors.toList());

        for (MechanicSeedDto mechanic : mechanics) {
            stringBuilder.append(System.lineSeparator());

            if (this.mechanicRepository.findFirstByEmail(mechanic.getEmail()).isPresent() ||
                    this.mechanicRepository.findFirstByFirstName(mechanic.getFirstName()).isPresent() ||
                    !this.validationUtil.isValid(mechanic)) {
                stringBuilder.append(String.format(INVALID_FORMAT, MECHANIC));
                continue;
            }

            this.mechanicRepository.save(this.modelMapper.map(mechanic, Mechanic.class));

            stringBuilder.append(String.format(SUCCESSFUL_FORMAT,
                    MECHANIC,
                    mechanic.getFirstName(),
                    mechanic.getLastName()));
        }

        return stringBuilder.toString().trim();
    }
}
