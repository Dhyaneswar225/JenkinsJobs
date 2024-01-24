package com.shutterfly.services.example.repo;
import com.shutterfly.services.example.model.Joke;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JokeRepository extends MongoRepository<Joke, String> {
    // Additional custom queries if needed
}