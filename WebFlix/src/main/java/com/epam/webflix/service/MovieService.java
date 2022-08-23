package com.epam.webflix.service;

import com.epam.webflix.exception.MissingMovieException;
import com.epam.webflix.model.Movie;
import com.epam.webflix.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MovieService {


    private final MovieRepository movieRepository;

    public Movie getMovieById(String id) {
        Movie movie = movieRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new MissingMovieException(Movie.class, id));
        log.info("Found a movie with this id: {}, Title: {}", id, movie.getTitle());
        return movie;
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie createMovie(String title, Integer releaseYear, Integer length) {
        Movie newMovie = movieRepository.save(Movie.builder()
                .title(title)
                .releaseYear(releaseYear)
                .length(length)
                .build());

        log.info("Saving new movie: {}", newMovie);
        return newMovie;
    }

    public Movie updateMovie(String movieId, String title, Integer releaseYear, Integer length) {
        Movie movie = movieRepository.findById(movieId).stream()
                .findFirst()
                .orElseThrow(() -> new MissingMovieException(Movie.class, movieId));
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setLength(length);

        movieRepository.save(movie);

        return movie;
    }

    public List<Movie> getMoviesByTitle(String title) {
        List<Movie> movies = movieRepository.findMovieByTitleContains(title);
        log.info("Found {} movie(s) with this title: {}", movies.size(), title);
        return movies;
    }

    public List<Movie> getMoviesByReleaseYearSortingByTitle(Integer releaseYear) {
        List<Movie> movies = movieRepository.findMovieByReleaseYear(releaseYear, Sort.by("title"));
        log.info("Found {} movie(s) with this releaseYear: {}", movies.size(), releaseYear);
        return movies;
    }

    public List<Movie> getMoviesByMinimumLength(Integer length) {
        List<Movie> movies = movieRepository.findMovieByLengthGreaterThanEqual(length);
        log.info("Found {} movie(s) with this minimum length: {}", movies.size(), length);
        return movies;
    }

    public List<Movie> getMoviesByMaximumLength(Integer length) {
        List<Movie> movies = movieRepository.findMovieByLengthLessThanEqual(length);
        log.info("Found {} movie(s) with this maximum length: {}", movies.size(), length);
        return movies;
    }
}
