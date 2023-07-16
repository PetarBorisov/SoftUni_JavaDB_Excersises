package com.example.springdto.Service.impl;


import com.example.springdto.DTOExersize.Config.model.dto.UserLoginDto;
import com.example.springdto.DTOExersize.Config.model.dto.UserRegisterDto;
import com.example.springdto.Entity.Game;
import com.example.springdto.Entity.User;
import com.example.springdto.Repository.GameRepository;
import com.example.springdto.Repository.UserRepository;
import com.example.springdto.Service.UserService;
import com.example.springdto.Util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final BufferedReader bufferedReader;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final GameRepository gameRepository;
    private static User loggedInUser;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, GameRepository gameRepository) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gameRepository = gameRepository;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("WRONG confirm password!");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violations = validationUtil.getViolations(userRegisterDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        User user = modelMapper.map(userRegisterDto, User.class);
        userRepository.save(user);

        System.out.printf("%s was registered%n", user.getFullName());
    }

    @Override
    public void loginService(UserLoginDto userLoginDto) {
        Set<ConstraintViolation<UserLoginDto>> violations = validationUtil.getViolations(userLoginDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        User user = (User) userRepository
                .findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword())
                .orElse(null);

        if (user == null) {
            System.out.println("Incorrect username / password");
            return;
        }

        loggedInUser = user;

        System.out.printf("Successfully logged in %s%n", loggedInUser.getFullName());
    }

    @Override
    public void logoutUser() {
        String loggedOutUser = null;
        if (loggedInUser != null) {
            loggedOutUser = loggedInUser.getFullName();
        }

        if (loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in.");
            return;
        } else {
            loggedInUser = null;
        }

        System.out.printf("User %s successfully logged out%n", loggedOutUser);
    }

    @Override
    public void printGamesBoughtByLoggedUser() throws IOException {

        System.out.println("Please enter title of game you want to purchase or 'stop' to end purchasing:");
        String gameTitle = bufferedReader.readLine();

        while (!gameTitle.equals("stop")) {
            purchaseBook(gameTitle);

            System.out.println("Please enter title of game you want to purchase or 'stop' to end purchasing:");
            gameTitle = bufferedReader.readLine();
        }

        Set<Game> purchasedGames = loggedInUser.getListGames();

        for (Game purchasedGame : purchasedGames) {
            System.out.println(purchasedGame.getTitle());
        }
    }

    @Override
    public void purchaseBook(String gameTitle) {
        List<Game> game =
                gameRepository
                        .findAll()
                        .stream()
                        .filter(currentGame -> currentGame.getTitle().equals(gameTitle))
                        .limit(1)
                        .collect(Collectors.toList());

        loggedInUser.getListGames().add(game.get(0));
    }
}
