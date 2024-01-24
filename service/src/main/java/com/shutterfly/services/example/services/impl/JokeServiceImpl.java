package com.shutterfly.services.example.services.impl;

import com.shutterfly.services.example.dao.api.JokeDao;
import com.shutterfly.services.example.model.Joke;
import com.shutterfly.services.example.services.api.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JokeServiceImpl implements JokeService {
    private final JokeDao jokeDao;

    @Autowired
    public JokeServiceImpl(JokeDao jokeDao) {
        this.jokeDao = jokeDao;
    }

    @Override
    public Joke getRandomJoke() {
         Joke j1= jokeDao.getRandomJoke();
         jokeDao.saveJoke(j1);
        return j1;
    }
    @Override
    public Joke getJokeById(String id) {
        Joke j2=jokeDao.getJokeById(id);
        return j2;
    }
    @Override
    public List<Joke> getAllJokes() {
        return jokeDao.getAllJokes();
    }

    @Override
    public void saveJoke(Joke newJoke) {
        jokeDao.saveJoke(newJoke);
    }

    @Override
    public Joke updateJoke(String id, String setup, String punchline) {
        jokeDao.updateJoke(id,setup,punchline);
        return null;
    }

    @Override
    public void deleteAllJokes() {
        jokeDao.deleteAllJokes();
    }

    @Override
    public void deleteById1(String id) {
jokeDao.deleteById1(id);
    }

}