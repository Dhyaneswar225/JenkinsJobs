package com.shutterfly.services.example.dao.impl;
import com.shutterfly.services.example.dao.api.JokeDao;
import com.shutterfly.services.example.model.Joke;
import com.shutterfly.services.example.repo.JokeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

@Repository
public class JokeDaoImpl implements JokeDao {
    private final JokeRepository jokeRepository;

    @Autowired
    public JokeDaoImpl(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    @Override
    public void saveJoke(Joke joke) {
        jokeRepository.save(joke);
    }

    @Override
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }
    @Override
    public Joke getRandomJoke() {
        try {
            // Create a RestTemplate with SSL verification disabled
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(getRequestFactoryWithDisabledSSLVerification());

            // Make the request to the joke API
            String apiUrl = "https://official-joke-api.appspot.com/random_joke";
            return restTemplate.getForObject(apiUrl, Joke.class);
        } catch (Exception e) {
            // Handle exceptions
            return null;
        }
    }

    @Override
    public Joke getJokeById(String id) {
        return jokeRepository.findById(id).orElse(null);
    }

    @Override
    public Joke updateJoke(String id, String setup, String punchline) {
        Optional<Joke> optionalJoke = jokeRepository.findById(id);
        System.out.println("Optional joke:"+optionalJoke);
        if (optionalJoke.isPresent()) {
            System.out.println("Entered optional joke");
            Joke existingJoke = optionalJoke.get();
            existingJoke.setSetup(setup);
            existingJoke.setPunchline(punchline);
            System.out.println("existing joke id:"+existingJoke.getId());
            System.out.println("existing joke setup:"+existingJoke.getSetup());
            System.out.println("existing joke punchline:"+existingJoke.getSetup());
            System.out.println("existing joke type:"+existingJoke.getType());
            jokeRepository.save(existingJoke); // Update the existing joke
        } else {
            // Handle the case where the joke with the given id is not found
            throw new RuntimeException("Joke with id " + id + " not found");
        }
        return null;
    }

    @Override
    public void deleteAllJokes() {
        jokeRepository.deleteAll();
    }

    @Override
    public void deleteById1(String id) {
        jokeRepository.deleteById(id);
    }

    private ClientHttpRequestFactory getRequestFactoryWithDisabledSSLVerification() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException, IOException {
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                    ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };
    }
}