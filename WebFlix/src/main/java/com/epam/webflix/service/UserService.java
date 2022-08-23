package com.epam.webflix.service;

import com.epam.webflix.exception.MissingUserException;
import com.epam.webflix.model.Movie;
import com.epam.webflix.model.User;
import com.epam.webflix.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MovieService movieService;

    public User createUser(String userName) {
        User newUser = userRepository.save(User.builder()
                .userName(userName)
                .build());
        log.info("New user created: {}", newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Listing users, found {} user(s).", users.size());
        return users;
    }

    public User getUserById(String id) {
        User user = userRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new MissingUserException(User.class, id));

        log.info("Result of the search: {}", user.getUserName());
        return user;
    }

    public User addFavouriteMovieToUser(String userId, String movieId) {
        User user = getUserById(userId);
        Movie movie = movieService.getMovieById(movieId);

        List<Movie> favouriteMovies = user.getFavouriteMovies();

        if (favouriteMovies == null) {
            user.setFavouriteMovies(List.of(movie));
            userRepository.save(user);
        } else if (!favouriteMovies.contains(movie)) {
            favouriteMovies.add(movie);
            user.setFavouriteMovies(favouriteMovies);
            userRepository.save(user);
            log.info("Successfully added a new favourite movie. Already has {} favourite movie(s).", user.getFavouriteMovies().size());
        } else {
            log.info("User already added this movie to the favourite movie list.");
        }
        return user;
    }
}
