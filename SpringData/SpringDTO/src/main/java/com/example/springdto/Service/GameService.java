package com.example.springdto.Service;



import com.example.springdto.DTOExersize.Config.model.dto.GameAddDto;

import java.math.BigDecimal;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(Long gameId, BigDecimal price, Double size);

    void deleteGame(Long gameId);

    void printAllGamesTitlesAndPrices();

    void printDetailGame(String gameTitle);
}
