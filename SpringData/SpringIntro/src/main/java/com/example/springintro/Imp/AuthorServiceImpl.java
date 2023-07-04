package com.example.springintro.Imp;

import com.example.springintro.Entity.Author;
import com.example.springintro.repository.AuthorRepository;
import com.example.springintro.service.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHOR_FILES = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (authorRepository.count() > 0){
            return;
        }

       Files.readAllLines(Path.of(AUTHOR_FILES))
               .forEach(row-> {
                   String[] fullName = row.split("\\s+");
                   Author author = new Author(fullName[0], fullName[1]);

                   authorRepository.save(author);

               });

    }

    @Override
    public Author getRandomAuthor() {
        Long randomId = ThreadLocalRandom
                .current().nextLong(1, authorRepository.count() + 1);

        return authorRepository.findById(randomId)
                .orElse(null);
    }
}
