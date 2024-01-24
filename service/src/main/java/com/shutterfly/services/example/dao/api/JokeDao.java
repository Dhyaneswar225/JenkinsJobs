package com.shutterfly.services.example.dao.api;
import com.shutterfly.services.example.model.Joke;

import java.util.List;

public interface JokeDao {
    void saveJoke(Joke joke);

    List<Joke> getAllJokes();
    Joke getRandomJoke();
    Joke getJokeById(String id);
    Joke updateJoke(String id, String setup, String punchline);

    void deleteAllJokes();

    void deleteById1(String id);
}