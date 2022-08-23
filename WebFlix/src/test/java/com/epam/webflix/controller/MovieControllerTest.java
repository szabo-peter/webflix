package com.epam.webflix.controller;

import com.epam.webflix.model.Movie;
import com.epam.webflix.service.MovieService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    private static final String GIVEN_TITLE = "Test title";
    private static final Integer GIVEN_LENGTH = 120;
    private static final Integer GIVEN_RELEASE_YEAR = 2021;
    private static final String GIVEN_ID = "12345678-1234-1234-1234-123456789abc";

    @MockBean
    MovieService movieService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllMovies() throws Exception {
        Movie givenMovie = givenMovie(GIVEN_ID, GIVEN_TITLE, GIVEN_LENGTH, GIVEN_RELEASE_YEAR);
        List<Movie> movies = List.of(givenMovie);

        whenGetAllMoviesCalled(movies);

        thenResultShouldBePageOfMovies(givenMovie);
    }

    @Test
    void testGetMovieById() throws Exception {
        Movie givenMovie = givenMovie(GIVEN_ID, GIVEN_TITLE, GIVEN_LENGTH, GIVEN_RELEASE_YEAR);

        whenGetMovieByIdCalled(GIVEN_ID, givenMovie);

        thenResultShouldBeTheProperMovieWithOk(GIVEN_ID, givenMovie);
    }

    @Test
    void testSaveMovie() throws Exception {
        Movie givenMovie = givenMovie(GIVEN_ID, GIVEN_TITLE, GIVEN_LENGTH, GIVEN_RELEASE_YEAR);

        whenSaveMovieCalled(givenMovie);

        thenResultShouldBeAMovieWithCreated(givenMovie);
    }

    private Movie givenMovie(String id, String title, Integer length, Integer releaseYear) {
        return Movie.builder()
                .id(id)
                .title(title)
                .releaseYear(releaseYear)
                .length(length)
                .build();
    }

    private void whenGetMovieByIdCalled(String givenId, Movie givenMovie) {
        Mockito.when(movieService.getMovieById(givenId)).thenReturn(givenMovie);
    }

    private void whenGetAllMoviesCalled(List<Movie> movies) {
        Page<Movie> moviePage = new PageImpl<>(movies);
        Mockito.when(movieService.getAllMovies(PageRequest.of(0, 1))).thenReturn(moviePage);
    }

    private void whenSaveMovieCalled(Movie movie) {
        Mockito.when(movieService.createMovie("Test title", 2021, 120)).thenReturn(movie);
    }

    private void thenResultShouldBePageOfMovies(Movie movie) throws Exception {
        mockMvc.perform(get("/api/movies/")
                        .queryParam("page", "0")
                        .queryParam("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies[0].id", Matchers.is(movie.getId())))
                .andExpect(jsonPath("$.movies[0].title", Matchers.is(movie.getTitle())))
                .andExpect(jsonPath("$.movies[0].length", Matchers.is(movie.getLength())))
                .andExpect(jsonPath("$.movies[0].releaseYear", Matchers.is(movie.getReleaseYear())))
                .andExpect(jsonPath("$.totalPages", Matchers.is(1)))
                .andExpect(jsonPath("$.currentPage", Matchers.is(0)))
                .andExpect(jsonPath("$.totalItems", Matchers.is(1)));
    }

    private void thenResultShouldBeTheProperMovieWithOk(String givenId, Movie movie) throws Exception {
        mockMvc.perform(get("/api/movies/{givenId}", givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(4)))
                .andExpect(jsonPath("$.title", Matchers.is(movie.getTitle())))
                .andExpect(jsonPath("$.releaseYear", Matchers.is(movie.getReleaseYear())))
                .andExpect(jsonPath("$.length", Matchers.is(movie.getLength())))
                .andExpect(jsonPath("$.id", Matchers.is(movie.getId())));
    }

    private void thenResultShouldBeAMovieWithCreated(Movie movie) throws Exception {
        mockMvc.perform(post("/api/movies")
                        .queryParam("title", movie.getTitle())
                        .queryParam("releaseYear", movie.getReleaseYear().toString())
                        .queryParam("length", movie.getLength().toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", Matchers.is(movie.getTitle())))
                .andExpect(jsonPath("$.releaseYear", Matchers.is(movie.getReleaseYear())))
                .andExpect(jsonPath("$.length", Matchers.is(movie.getLength())))
                .andExpect(jsonPath("$.id", Matchers.is(movie.getId())));
    }
}