package com.shutterfly.services.example.endpoints;
import com.shutterfly.services.example.model.Joke;
import com.shutterfly.services.example.model.JokeUpdateRequest;
import com.shutterfly.services.example.services.api.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//http://localhost:8080/_internal/swagger-ui/index.html
@Controller
public class JokeController {
    private final JokeService jokeService;

    @Autowired
    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping("/random-joke")
    public String getRandomJoke(Model model) {
        Joke joke = jokeService.getRandomJoke();
        model.addAttribute("joke", joke);
        return "random-joke"; // Thymeleaf template name
    }

    @GetMapping("/all-jokes")
    public String getAllJokes(Model model) {
        List<Joke> jokes = jokeService.getAllJokes();
        model.addAttribute("jokes", jokes);
        return "all-jokes"; // Thymeleaf template name
    }

    @GetMapping("/addJoke")
    public String showAddJokeForm(Model model) {
        model.addAttribute("newJoke", new Joke());
        return "addJoke";
    }
@GetMapping("/")
public String home(){
        return "home";
}
    @PostMapping("/jokes")
    public String addJoke(@RequestBody Joke newJoke) {
        jokeService.saveJoke(newJoke);
        return "redirect:/";
    }
    @GetMapping("/get-joke/{id}")
    public String getJoke(@PathVariable String id,Model model) {
        Joke joke1 = jokeService.getJokeById(id);
        model.addAttribute("joke1", joke1);
        return "get-joke";
    }
    @GetMapping("/update-joke/{id}")
    public String showUpdateJokeForm(@PathVariable String id, Model model) {
        Joke joke = jokeService.getJokeById(id);
        model.addAttribute("joke", joke);
        return "update-joke";
    }

    @PostMapping("/update-joke/{id}")
    public String updateJoke(@PathVariable String id, @RequestBody JokeUpdateRequest JUR) {
        System.out.println("Entered post");
        System.out.println("Setup:"+JUR.getSetup());
        System.out.println("Get Punchline:"+JUR.getPunchline());
        jokeService.updateJoke(id, JUR.getSetup(), JUR.getPunchline());
        return "redirect:/";
    }
    @GetMapping("/delete-all-jokes")
   public String deleteAllJokes(){
        jokeService.deleteAllJokes();
        return "redirect:/";
    }
    @GetMapping("/delete-joke/{id}")
    public String deleteJokeById(@PathVariable String id){
        jokeService.deleteById1(id);
        return "redirect:/";
    }
}