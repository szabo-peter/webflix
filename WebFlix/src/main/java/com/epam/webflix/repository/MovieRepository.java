package com.epam.webflix.repository;

import com.epam.webflix.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    Page<Movie> findAll(Pageable pageable);

    Optional<Movie> findById(String id);

    Movie save(Movie movie);

    List<Movie> findMovieByTitleContains(String title);

    List<Movie> findMovieByReleaseYear(Integer releaseYear, Sort sort);

    List<Movie> findMovieByLengthGreaterThanEqual(Integer length);

    List<Movie> findMovieByLengthLessThanEqual(Integer length);
}
