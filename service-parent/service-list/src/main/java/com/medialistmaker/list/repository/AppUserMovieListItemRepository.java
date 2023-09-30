package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMovieListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMovieListItemRepository extends JpaRepository<AppUserMovieListItem, Long>, AppUserListItemBaseRepository<AppUserMovieListItem> {

    AppUserMovieListItem getByAppUserIdAndMovieId(Long appUserId, Long movieId);
}
