package com.example.springdto;


import com.example.springdto.DTOExersize.Config.model.dto.GameAddDto;
import com.example.springdto.DTOExersize.Config.model.dto.UserLoginDto;
import com.example.springdto.DTOExersize.Config.model.dto.UserRegisterDto;
import com.example.springdto.Service.GameService;
import com.example.springdto.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader bufferedReader;
    private final UserService userService;
    private final GameService gameService;

    public CommandLineRunnerImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Please, enter your commands:");
            String[] commands = bufferedReader.readLine().split("\\|");

            switch (commands[0]) {
                case "RegisterUser" -> userService
                        .registerUser(new UserRegisterDto(commands[1],commands[2],commands[3],commands[4]));
                case "LoginUser" -> userService
                        .loginService(new UserLoginDto(commands[1], commands[2]));
                case "Logout" -> userService
                        .logoutUser();
                case "AddGame" -> gameService.addGame(new GameAddDto(commands[1],new BigDecimal(commands[2]),
                        Double.parseDouble(commands[3]),commands[4],commands[5],commands[6],
                        LocalDate.parse(commands[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                case "EditGame" -> gameService.editGame(Long.parseLong(commands[1]), new BigDecimal(commands[2]),
                        Double.parseDouble(commands[3]));
                case "DeleteGame" -> gameService.deleteGame(Long.parseLong(commands[1]));
                case "AllGames" -> gameService.printAllGamesTitlesAndPrices();
                case "DetailGame" -> gameService.printDetailGame(commands[1]);
                case "OwnedGames" -> userService.printGamesBoughtByLoggedUser();
            }
        }
    }
}
