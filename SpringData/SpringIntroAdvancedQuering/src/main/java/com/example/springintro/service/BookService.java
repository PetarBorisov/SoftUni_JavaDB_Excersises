package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldenBooksTitleWithCopies();

    List<String> findAllBooksWithPriceLower5AndHiger40();

    List<String> findAllBooksIsNotReleasedInThisYear(int year);


    List<String> findAllBooksReleasedBeforeDate(LocalDate localDate);


    List<String> bookSearch(String input);

    List<String> findAllTittleWithLastNameStartWith(String input);


    int findAllBooksTitleLongerThan(int number);


    List<String> findBookInfo(String bookTitle);
}
