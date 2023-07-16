package com.example.springdto.Service;



import com.example.springdto.DTOExersize.Config.model.dto.UserLoginDto;
import com.example.springdto.DTOExersize.Config.model.dto.UserRegisterDto;

import java.io.IOException;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginService(UserLoginDto userLoginDto);

    void logoutUser();

    void printGamesBoughtByLoggedUser() throws IOException;

    void purchaseBook(String gameTitle);
}
