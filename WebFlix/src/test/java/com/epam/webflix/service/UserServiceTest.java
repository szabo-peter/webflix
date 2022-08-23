package com.epam.webflix.service;

import com.epam.webflix.model.Movie;
import com.epam.webflix.model.User;
import com.epam.webflix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String USER_1_USER_NAME = "Test User Name";
    private static final String USER_2_USER_NAME = "Other User Name";
    private static final String USER_1_ID = "12345678-1234-1234-1234-123456789abc";
    private static final String USER_2_ID = "abc45678-1234-1234-1234-123456789abc";
    private static final String MOVIE_ID = "12345678-1234-1234-1234-123456789def";
    private static final String MOVIE_TITLE = "Test Title";
    private static final Integer MOVIE_RELEASE_YEAR = 2020;
    private static final Integer MOVIE_LENGTH = 120;

    private static final Movie MOVIE = Movie.builder()
            .id(MOVIE_ID)
            .length(MOVIE_LENGTH)
            .title(MOVIE_TITLE)
            .releaseYear(MOVIE_RELEASE_YEAR)
            .build();
    private static final User USER_1 = User.builder()
            .id(USER_1_ID)
            .userName(USER_1_USER_NAME)
            .build();

    private static final User USER_2 = User.builder()
            .id(USER_2_ID)
            .userName(USER_2_USER_NAME)
            .build();

    private List<User> expectedUsers;
    private List<User> actualListOfUsers;
    private User actualUser;

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    MovieService movieService;

    @BeforeEach
    void init() {
        expectedUsers = null;
        actualListOfUsers = null;
        actualUser = null;
    }

    @Test
    void testFindAllUsers() {
        givenUserRepositoryWithUsers(USER_1, USER_2);

        whenFindAllIsCalled();

        thenResultShouldBe(USER_1, USER_2);
    }

    @Test
    void testGetUserById() {
        givenUserRepositoryWithUser(USER_1, USER_1_ID);

        whenFindByIdIsCalled(USER_1_ID);

        thenResultShouldBe(USER_1);

    }

    @Test
    void testCreateUser() {
        givenUserRepositoryWithUser(USER_1);

        whenCreateUserIsCalled(USER_1_USER_NAME);

        thenResultShouldBe(USER_1);
    }

    @Test
    void testAddFavouriteMovieToUser() {
        givenUserRepositoryWithUser(USER_1, USER_1_ID);
        givenMovieServiceWithMovie(MOVIE, MOVIE_ID);

        whenAddFavouriteMovieIsCalled(USER_1_ID, MOVIE_ID);

        thenResultShouldBe(MOVIE_TITLE, MOVIE_ID);

    }

    private void givenUserRepositoryWithUsers(User...users) {
        when(userRepository.findAll()).thenReturn(Arrays.asList(users));
    }

    private void givenUserRepositoryWithUser(User user, String userId) {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    }

    private void givenMovieServiceWithMovie(Movie movie, String movieId) {
        when(movieService.getMovieById(movieId)).thenReturn(movie);
    }

    private void givenUserRepositoryWithUser(User user) {
        User newUser = User.builder()
                .userName(user.getUserName())
                .build();
        when(userRepository.save(newUser)).thenReturn(user);
    }

    private void whenFindAllIsCalled() {
        actualListOfUsers = userService.getAllUsers();
    }

    private void whenFindByIdIsCalled(String userId) {
        actualUser = userService.getUserById(userId);
    }

    private void whenCreateUserIsCalled(String userName) {
        actualUser = userService.createUser(userName);
    }

    private void whenAddFavouriteMovieIsCalled(String userId, String movieId) {
        actualUser = userService.addFavouriteMovieToUser(userId, movieId);
    }

    private void thenResultShouldBe(User...users) {
        expectedUsers = List.of(users);
        assertEquals(expectedUsers, actualListOfUsers);
    }

    private void thenResultShouldBe(User expectedUser) {
        assertEquals(expectedUser, actualUser);
    }

    private void thenResultShouldBe(String expectedMovieTitle, String expectedMovieId) {
        assertEquals(expectedMovieTitle, actualUser.getFavouriteMovies().get(0).getTitle());
        assertEquals(expectedMovieId, actualUser.getFavouriteMovies().get(0).getId());
    }
}