package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskQueryDto;
import softuni.exam.models.dto.TaskSeedDto;
import softuni.exam.models.dto.TasksSeedRootDto;
import softuni.exam.models.entity.*;
import softuni.exam.repository.CarsRepository;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.repository.PartsRepository;
import softuni.exam.repository.TasksRepository;
import softuni.exam.service.TasksService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.models.Constants.*;

@Service
public class TasksServiceImpl implements TasksService {
    private static String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml/";

    private final TasksRepository taskRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final CarsRepository carsRepository;
    private final PartsRepository partsRepository;
    private final MechanicsRepository mechanicsRepository;
   @Autowired
    public TasksServiceImpl(TasksRepository taskRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, CarsRepository carsRepository, PartsRepository partsRepository, MechanicsRepository mechanicsRepository) {
        this.taskRepository = taskRepository;
       this.modelMapper = modelMapper;
       this.validationUtil = validationUtil;
       this.xmlParser = xmlParser;
       this.carsRepository = carsRepository;
       this.partsRepository = partsRepository;
       this.mechanicsRepository = mechanicsRepository;
   }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<TaskSeedDto> tasks =
                this.xmlParser
                        .fromFile(Path.of(TASKS_FILE_PATH).toString(), TasksSeedRootDto.class)
                        .getTasks();

        for (TaskSeedDto task : tasks) {
            stringBuilder.append(System.lineSeparator());

            if (this.validationUtil.isValid(task)) {
                final Optional<Mechanic> mechanic =
                        this.mechanicsRepository.findAllByFirstName(task.getMechanic().getFirstName());
                final Optional<Car> car =
                        this.carsRepository.findById(task.getCar().getId());
                final Optional<Part> part =
                        this.partsRepository.findById(task.getPart().getId());

                if (car.isEmpty() || part.isEmpty() || mechanic.isEmpty()) {
                    stringBuilder.append(String.format(INVALID_FORMAT, TASK));
                    continue;
                }

                final Task taskToSave = this.modelMapper.map(task, Task.class);
                taskToSave.setMechanic(mechanic.get());
                taskToSave.setPart(part.get());
                taskToSave.setCar(car.get());

                this.taskRepository.save(taskToSave);
                stringBuilder.append(String.format(SUCCESSFUL_FORMAT, TASK, task.getPrice().setScale(2), "").trim());
                continue;
            }
            stringBuilder.append(String.format(INVALID_FORMAT, TASK));
        }

        return stringBuilder.toString().trim();
    }



    @Override
    public String getCoupeCarTasksOrderByPrice() {
        return this.taskRepository.findAllByCarCarTypeOrderByPriceDesc(CarType.coupe)
                .stream()
                .map(task -> modelMapper.map(task, TaskQueryDto.class))
                .map(TaskQueryDto::toString)
                .collect(Collectors.joining())
                .trim();
    }
}
