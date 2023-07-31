package com.example.xml.service;

import com.example.xml.model.dto.UserSeedDTO;
import com.example.xml.model.dto.UserViewRootDto;
import com.example.xml.model.entity.User;

import java.util.List;

public interface UserService {
    long getCount();

    void seedUsers(List<UserSeedDTO> users);

    User getRandomUser();

    UserViewRootDto findWithMoreThanOneSoldProduct();

}
