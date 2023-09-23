package com.medialistmaker.movie.repository;

import com.medialistmaker.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie getByApiCode(String apiCode);

    @Query("SELECT m FROM Movie m WHERE m.id IN(:movieIds)")
    List<Movie> getByIds(@Param("movieIds") List<Long> movieIds);
}
