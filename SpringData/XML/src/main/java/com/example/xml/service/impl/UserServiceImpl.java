package com.example.xml.service.impl;

import com.example.xml.model.dto.UserSeedDTO;
import com.example.xml.model.dto.UserViewRootDto;
import com.example.xml.model.dto.UserWittProductDto;
import com.example.xml.model.entity.User;
import com.example.xml.repository.UserRepository;
import com.example.xml.service.UserService;
import com.example.xml.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final ModelMapper modelMapper;
   private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }

    @Override
    public void seedUsers(List<UserSeedDTO> users) {
        users
                .stream()
                .filter(validationUtil::isValid)
                .map(userSeedDTO -> modelMapper.map(userSeedDTO, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User getRandomUser() {
        long randomId = ThreadLocalRandom.current().nextLong(1, userRepository.count() + 1);
        return userRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public UserViewRootDto findWithMoreThanOneSoldProduct() {
        UserViewRootDto userViewRootDto = new UserViewRootDto();

      userViewRootDto.setProducts(userRepository
              .findAllUserWithMoreThanOneSoldProduct()
              .stream()
              .map(user -> modelMapper.map(user, UserWittProductDto.class))
              .collect(Collectors.toList()));

        return userViewRootDto;
    }
}
