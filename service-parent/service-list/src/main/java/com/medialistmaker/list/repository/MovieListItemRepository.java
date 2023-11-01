package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.MovieListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieListItemRepository extends JpaRepository<MovieListItem, Long>, ListItemBaseRepository<MovieListItem> {

    MovieListItem getByAppUserIdAndMovieId(Long appUserId, Long movieId);

}
