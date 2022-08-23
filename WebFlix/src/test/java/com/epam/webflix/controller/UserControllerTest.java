package com.epam.webflix.controller;

import com.epam.webflix.model.Movie;
import com.epam.webflix.model.User;
import com.epam.webflix.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;


    private static final String GIVEN_USER_NAME = "Test user";
    private static final String GIVEN_USER_ID = "12345678-1234-1234-1234-123456789abc";
    private static final String GIVEN_MOVIE_ID = "12345678-1234-1234-1234-123456789def";
    private static final List<Movie> EMPTY_FAVOURITE_LIST = Collections.emptyList();

    @Test
    void testGetAllUsers() throws Exception {
        User givenUser = givenUser(GIVEN_USER_NAME, GIVEN_USER_ID, EMPTY_FAVOURITE_LIST);
        List<User> users = List.of(givenUser);

        whenGetAllUsersCalled(users);

        thenResultShouldBeListOfUsers(givenUser);
    }

    @Test
    void testGetUserById() throws Exception {
        User givenUser = givenUser(GIVEN_USER_NAME, GIVEN_USER_ID, EMPTY_FAVOURITE_LIST);

        whenGetUserByIdCalled(GIVEN_USER_ID, givenUser);

        thenResultShouldBeTheProperUserWithOk(GIVEN_USER_ID, givenUser);
    }

    @Test
    void testCreateUser() throws Exception {
        User givenUser = givenUser(GIVEN_USER_NAME, GIVEN_USER_ID, EMPTY_FAVOURITE_LIST);

        whenSaveUserCalled(givenUser);

        thenResultShouldBeAUserWithCreated(givenUser);
    }

    @Test
    void testAddFavouriteMovieToUser() throws Exception {
        Movie givenMovie = Movie.builder().id(GIVEN_MOVIE_ID).title("Test title").length(180).releaseYear(2020).build();
        User givenUser = givenUser(GIVEN_USER_NAME, GIVEN_USER_ID, List.of(givenMovie));

        Mockito.when(userService.addFavouriteMovieToUser(givenUser.getId(), GIVEN_MOVIE_ID)).thenReturn(givenUser);


        thenResultShouldBeTheUpdatedUserWithOk(GIVEN_MOVIE_ID, givenUser);
    }

    private User givenUser(String userName, String userId, List<Movie> movies) {
        return User.builder().id(userId).userName(userName).favouriteMovies(movies).build();
    }

    private void whenGetUserByIdCalled(String givenId, User givenUser) {
        Mockito.when(userService.getUserById(givenId)).thenReturn(givenUser);
    }

    private void whenGetAllUsersCalled(List<User> users) {
        Mockito.when(userService.getAllUsers()).thenReturn(users);
    }

    private void whenSaveUserCalled(User user) {
        Mockito.when(userService.createUser(GIVEN_USER_NAME)).thenReturn(user);
    }

    private void thenResultShouldBeListOfUsers(User user) throws Exception {
        mockMvc.perform(get("/api/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].userName", Matchers.is(user.getUserName())))
                .andExpect(jsonPath("$[0].id", Matchers.is(user.getId())))
                .andExpect(jsonPath("$.[0].favouriteMovies", Matchers.hasSize(0)));
    }

    private void thenResultShouldBeTheProperUserWithOk(String givenId, User user) throws Exception {
        mockMvc.perform(get("/api/users/{userId}", givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(3)))
                .andExpect(jsonPath("$.userName", Matchers.is(user.getUserName())))
                .andExpect(jsonPath("$.id", Matchers.is(user.getId())))
                .andExpect(jsonPath("$.favouriteMovies", Matchers.hasSize(0)));
    }

    private void thenResultShouldBeAUserWithCreated(User user) throws Exception {
        mockMvc.perform(post("/api/users")
                        .queryParam("userName", user.getUserName()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName", Matchers.is(user.getUserName())))
                .andExpect(jsonPath("$.id", Matchers.is(user.getId())))
                .andExpect(jsonPath("$.favouriteMovies", Matchers.hasSize(0)));
    }

    private void thenResultShouldBeTheUpdatedUserWithOk(String movieId, User user) throws Exception {
        mockMvc.perform(post("/api/users/{userId}/movies/{movieId}", user.getId(), movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(3)))
                .andExpect(jsonPath("$.userName", Matchers.is(user.getUserName())))
                .andExpect(jsonPath("$.id", Matchers.is(user.getId())))
                .andExpect(jsonPath("$.favouriteMovies", Matchers.hasSize(1)));
    }
}

