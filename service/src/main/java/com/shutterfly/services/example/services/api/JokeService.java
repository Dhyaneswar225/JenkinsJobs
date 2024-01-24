package com.shutterfly.services.example.services.api;

import com.shutterfly.services.example.model.Joke;

import java.util.List;

public interface JokeService {
    Joke getRandomJoke();

    List<Joke> getAllJokes();

    void saveJoke(Joke newJoke);
    Joke getJokeById(String id);
    Joke updateJoke(String id, String setup, String punchline);
    void deleteAllJokes();
    void deleteById1(String id);
}