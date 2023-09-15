package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicSeedDto;
import softuni.exam.models.dto.PartsSeedDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartsRepository;
import softuni.exam.service.PartsService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exam.models.Constants.*;
import static softuni.exam.models.Constants.MECHANIC;


@Service
public class PartsServiceImpl implements PartsService {
    private static String PART_FILE_PATH = "src/main/resources/files/json/parts.json";

    private final PartsRepository partRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;


@Autowired
    public PartsServiceImpl(PartsRepository partRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.partRepository = partRepository;
    this.validationUtil = validationUtil;
    this.modelMapper = modelMapper;
    this.gson = gson;

}

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PART_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<PartsSeedDto> parts =
                Arrays.stream(this.gson.fromJson(readPartsFileContent(), PartsSeedDto[].class))
                        .collect(Collectors.toList());

        for (PartsSeedDto part : parts) {
            stringBuilder.append(System.lineSeparator());

            if (this.partRepository.findFirstByPartName(part.getPartName()).isPresent() ||
                    !this.validationUtil.isValid(part)) {
                stringBuilder.append(String.format(INVALID_FORMAT, PART));
                continue;
            }

//          Successfully imported part Performance Battery - 455.38
            this.partRepository.save(this.modelMapper.map(part, Part.class));

            stringBuilder.append(String.format(SUCCESSFUL_FORMAT,
                    PART,
                    part.getPartName() + " -",
                    part.getPrice()));
        }


        return stringBuilder.toString().trim();
    }
}
