package com.epam.webflix.controller;

import com.epam.webflix.annotation.UUID;
import com.epam.webflix.model.User;
import com.epam.webflix.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUser(){
        List<User> users = userService.getAllUsers();
        log.info("Found {} user(s).", users.size());
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestParam(name = "userName") @NotBlank @Size(min = 5, max = 20 ) String userName){
        return userService.createUser(userName);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable @UUID String id){
        return userService.getUserById(id);
    }

    @PostMapping("/{userId}/movies/{movieId}")
    public User addFavouriteMovieToUser(@PathVariable @UUID String userId,
                                        @PathVariable @UUID String movieId){
        return userService.addFavouriteMovieToUser(userId,movieId);
    }
}
