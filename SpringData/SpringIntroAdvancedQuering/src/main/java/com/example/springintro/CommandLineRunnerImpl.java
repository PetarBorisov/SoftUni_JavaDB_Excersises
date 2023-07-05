package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        //printAllBooksAfterYear(2000);
        // printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        // printAllAuthorsAndNumberOfTheirBooks();
        // pritnALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Select Exercise :");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum) {
            case 1 -> booksTitleByAgeRestriction();
            case 2 -> goldenBook();
            case 3 -> BooksOfPrice();
            case 4 -> NotReleasedBooks();
            case 5 -> BooksReleasedBeforeDate();
            case 6 -> AuthorSearch();
            case 7 -> BooksSearch();
            case 8 -> BookTitleSearch();
            case 9 -> CountBooks();
            case 10 -> TotalBookCopies();
            case 11 -> ReduceBook();


        }
    }

    private void ReduceBook() throws IOException {
        System.out.println("Enter book title:");
        String bookTitle = bufferedReader.readLine();
        bookService.findBookInfo(bookTitle)
                .forEach(System.out::println);
    }

    private void TotalBookCopies() {
        authorService.findAllNumberBookCopies()
                .forEach(System.out::println);
    }

    private void CountBooks() throws IOException {
        System.out.println("Enter number:");
        int number = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.findAllBooksTitleLongerThan(number));
    }

    private void BookTitleSearch() throws IOException {
        System.out.println("Enter Input String:");
        String input = bufferedReader.readLine().toUpperCase();

        bookService.findAllTittleWithLastNameStartWith(input)
                .forEach(System.out::println);
    }

    private void BooksSearch() throws IOException {
        System.out.println("Enter String Input:");
        String input = bufferedReader.readLine().toUpperCase();

        bookService.bookSearch(input)
                .forEach(System.out::println);

    }

    private void AuthorSearch() throws IOException {
        System.out.println("Enter String:");
        String input = bufferedReader.readLine();

        authorService.findAllAuthorWithFirstNameEndStringInput(input)
                .forEach(System.out::println);
    }

    private void BooksReleasedBeforeDate() throws IOException {
        System.out.println("Enter Date in format dd-MM-yyyy:");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));


        bookService.findAllBooksReleasedBeforeDate(localDate)
                .forEach(System.out::println);
    }

    private void NotReleasedBooks() throws IOException {
        System.out.println("Enter Year:");
        int year = Integer.parseInt(bufferedReader.readLine());

        bookService.findAllBooksIsNotReleasedInThisYear(year)
                .forEach(System.out::println);
    }

    private void BooksOfPrice() {

        bookService.findAllBooksWithPriceLower5AndHiger40()
                .forEach(System.out::println);
    }

    private void goldenBook() {
        bookService.findAllGoldenBooksTitleWithCopies()
                .forEach(System.out::println);
    }

    private void booksTitleByAgeRestriction() throws IOException {
        System.out.println("Enter Age Restriction:");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());
       bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
               .forEach(System.out::println);

    }

    private void pritnALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
