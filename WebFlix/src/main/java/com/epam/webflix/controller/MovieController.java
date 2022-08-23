package com.epam.webflix.controller;

import com.epam.webflix.annotation.UUID;
import com.epam.webflix.model.Movie;
import com.epam.webflix.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Movie> pageMovies = movieService.getAllMovies(paging);
        List<Movie> movies = pageMovies.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("currentPage", pageMovies.getNumber());
        response.put("totalItems", pageMovies.getTotalElements());
        response.put("totalPages", pageMovies.getTotalPages());

        log.info("Found {} movies", movies.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{movieId}")
    public Movie getMovieById(@PathVariable @UUID String movieId) {
        return movieService.getMovieById(movieId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestParam(name = "title") @NotBlank String title,
                             @RequestParam(name = "releaseYear") @Min(1888) Integer releaseYear,
                             @RequestParam(name = "length") @Min(1) Integer length) {
        log.info("Save movie with {} title, {} release year, {} length", title, releaseYear, length);
        return movieService.createMovie(title, releaseYear, length);
    }

    @PutMapping("/{movieId}")
    public Movie updateMovie(@PathVariable @UUID String movieId,
                             @RequestParam(name = "title") @NotBlank String title,
                             @RequestParam(name = "releaseYear") @Min(1888) Integer releaseYear,
                             @RequestParam(name = "length") @Min(1) Integer length) {
        log.info("Updating movie with this id: {}", movieId);
        return movieService.updateMovie(movieId, title, releaseYear, length);
    }

    @GetMapping("/search/title")
    public List<Movie> getMovieByTitle(@RequestParam(name = "title") @NotBlank String title) {
        log.info("Searching movie with this title: {}", title);
        return movieService.getMoviesByTitle(title);
    }

    @GetMapping("/search/releaseYear")
    public List<Movie> getMovieByReleaseYearAndSortingByTitle(
            @RequestParam(name = "releaseYear") @Min(1888) Integer releaseYear) {
        log.info("Searching movie with this release year: {}", releaseYear);
        return movieService.getMoviesByReleaseYearSortingByTitle(releaseYear);
    }

    @GetMapping("/search/length")
    public List<Movie> getMovieByLength(
            @RequestParam(name = "minimumLength", defaultValue = "0") @Min(0) Integer minLength,
            @RequestParam(name = "maximumLength", defaultValue = "0") @Min(0) Integer maxLength) {

        Set<Movie> minimumMovieSet = new HashSet<>();
        Set<Movie> maximumMovieSet = new HashSet<>();


        if (minLength != 0) {
            log.info("Searching movie with minimum length: {}", minLength);
            minimumMovieSet.addAll(movieService.getMoviesByMinimumLength(minLength));
        }
        if (maxLength != 0) {
            log.info("Searching movie with maximum length: {}", maxLength);
            maximumMovieSet.addAll(movieService.getMoviesByMaximumLength(maxLength));
        }
        if (!maximumMovieSet.isEmpty() && minimumMovieSet.isEmpty()) {
            log.info("Found {} movie(s) in this search.", maximumMovieSet.size());
            return new ArrayList<>(maximumMovieSet);
        }
        if (maximumMovieSet.isEmpty() && !minimumMovieSet.isEmpty()) {
            log.info("Found {} movie(s) in this search.", minimumMovieSet.size());
            return new ArrayList<>(minimumMovieSet);
        } else {
            Set<Movie> intersection = new HashSet<>(maximumMovieSet);
            intersection.retainAll(minimumMovieSet);
            log.info("Found {} movie(s) in this search.", intersection.size());
            return new ArrayList<>(intersection);
        }
    }
}
