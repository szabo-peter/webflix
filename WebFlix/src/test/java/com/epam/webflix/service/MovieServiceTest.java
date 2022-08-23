package com.epam.webflix.service;

import com.epam.webflix.model.Movie;
import com.epam.webflix.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private static final String MOVIE_1_ID = "12345678-1234-1234-1234-123456789abc";
    private static final String MOVIE_2_ID = "12345678-1234-1234-1234-123456789def";
    private static final Integer MOVIE_1_LENGTH = 60;
    private static final Integer MOVIE_2_LENGTH = 120;
    private static final String MOVIE_1_TITLE = "Test title";
    private static final String MOVIE_2_TITLE = "Other Test title";
    private static final Integer MOVIE_1_RELEASE_YEAR = 2020;
    private static final Integer MOVIE_2_RELEASE_YEAR = 1980;

    private static final Movie MOVIE_1 = Movie.builder()
            .id(MOVIE_1_ID)
            .length(MOVIE_1_LENGTH)
            .title(MOVIE_1_TITLE)
            .releaseYear(MOVIE_1_RELEASE_YEAR)
            .build();
    private static final Movie MOVIE_2 = Movie.builder()
            .id(MOVIE_2_ID)
            .length(MOVIE_2_LENGTH)
            .title(MOVIE_2_TITLE)
            .releaseYear(MOVIE_2_RELEASE_YEAR)
            .build();
    private static final Movie UPDATED_MOVIE_2 = Movie.builder()
            .id(MOVIE_2_ID)
            .length(MOVIE_2_LENGTH)
            .title(MOVIE_1_TITLE)
            .releaseYear(MOVIE_1_RELEASE_YEAR)
            .build();
    private Page<Movie> expectedPageOfMovies;
    private Page<Movie> actualPageOfMovies;
    private Movie actualMovie;

    @InjectMocks
    MovieService movieService;

    @Mock
    MovieRepository movieRepository;

    @BeforeEach
    void init() {
        expectedPageOfMovies = null;
        actualPageOfMovies = null;
        actualMovie = null;
    }

    @Test
    void testFindAllMovies() {
        givenMovieRepositoryWithMovies(MOVIE_1, MOVIE_2);

        whenFindAllIsCalled();

        thenResultShouldBe(MOVIE_1, MOVIE_2);
    }

    @Test
    void testGetMovieById() {
        givenMovieRepositoryWithMovie(MOVIE_1, MOVIE_1_ID);

        whenFindByIdIsCalled(MOVIE_1_ID);

        thenResultShouldBe(MOVIE_1);
    }

    @Test
    void testCreateMovie() {
        givenMovieRepositoryWithMovie(MOVIE_1);

        whenCreateMovieIsCalled(MOVIE_1_TITLE, MOVIE_1_RELEASE_YEAR, MOVIE_1_LENGTH);

        thenResultShouldBe(MOVIE_1);
    }

    @Test
    void testUpdateMovie() {
        givenMovieRepositoryWithUpdatedMovie(MOVIE_2_ID, UPDATED_MOVIE_2);

        whenUpdateMovieIsCalled(MOVIE_2_ID, MOVIE_1_TITLE, MOVIE_1_RELEASE_YEAR, MOVIE_2_LENGTH);

        thenResultShouldBe(UPDATED_MOVIE_2);
    }

    @Test
    void testFindMovieByReleaseYear() {
        givenMovieRepositoryWithUpdatedMovie(MOVIE_2_ID, UPDATED_MOVIE_2);

        whenUpdateMovieIsCalled(MOVIE_2_ID, MOVIE_1_TITLE, MOVIE_1_RELEASE_YEAR, MOVIE_2_LENGTH);

        thenResultShouldBe(UPDATED_MOVIE_2);
    }

    private void givenMovieRepositoryWithMovies(Movie... movies) {
        List<Movie> movieList = List.of(movies);
        Page<Movie> moviePage = new PageImpl<>(movieList);
        when(movieRepository.findAll(PageRequest.of(0, 3))).thenReturn(moviePage);
    }

    private void givenMovieRepositoryWithMovie(Movie movie1, String movieId) {
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie1));
    }

    private void givenMovieRepositoryWithMovie(Movie movie) {
        Movie newMovie = Movie.builder()
                .length(movie.getLength())
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .build();
        when(movieRepository.save(newMovie)).thenReturn(movie);
    }

    private void givenMovieRepositoryWithUpdatedMovie(String id, Movie updatedMovie) {
        when(movieRepository.findById(id)).thenReturn(Optional.of(MOVIE_2));
        when(movieRepository.save(updatedMovie)).thenReturn(updatedMovie);
    }

    private void whenFindAllIsCalled() {
        actualPageOfMovies = movieService.getAllMovies(PageRequest.of(0, 3));
    }

    private void whenFindByIdIsCalled(String movieId) {
        actualMovie = movieService.getMovieById(movieId);
    }

    private void whenCreateMovieIsCalled(String title, Integer releaseYear, Integer length) {
        actualMovie = movieService.createMovie(title, releaseYear, length);
    }

    private void whenUpdateMovieIsCalled(String movieId, String title, Integer releaseYear, Integer length) {
        actualMovie = movieService.updateMovie(movieId, title, releaseYear, length);
    }

    private void thenResultShouldBe(Movie... movies) {
        List<Movie> movieList = List.of(movies);
        expectedPageOfMovies = new PageImpl<>(movieList);
        assertEquals(expectedPageOfMovies, actualPageOfMovies);
    }

    private void thenResultShouldBe(Movie expectedMovie) {
        assertEquals(expectedMovie, actualMovie);
    }
}