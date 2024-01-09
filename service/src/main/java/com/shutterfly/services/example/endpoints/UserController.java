package com.shutterfly.services.example.endpoints;

import com.shutterfly.services.example.model.User;
import com.shutterfly.services.example.services.api.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * @Operation is a Swagger annotation used to customize the documentation
     * generated at /api-docs. See Swagger docs for other annotations that may
     * be useful. For demonstrating authentication workflow we have added /users/** endpoint to
     * list of un-authenticated urls in application.yaml to by-pass the authorization workflow.
     */

    @Operation( summary="Retrieve users from the database",  // Short overview
            description = "Limited to the first 5 users currently.") // implementation details/notes
    @GetMapping("/users")
    public List<User> users() {
        return this.userService.findAllUsers();
    }

    @Operation( summary = "Retrieve user by id")
    @GetMapping("/users/{uid}")
    public User userById(@PathVariable final String uid) {
        return this.userService.findUserById(uid);
    }
}
