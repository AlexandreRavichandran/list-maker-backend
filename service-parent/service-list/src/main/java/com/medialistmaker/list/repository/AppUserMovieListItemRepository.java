package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMovieListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserMovieListItemRepository extends JpaRepository<AppUserMovieListItem, Long> {

    List<AppUserMovieListItem> getByAppUserIdOrderBySortingOrderAsc(Long appUserId);

    AppUserMovieListItem getByAppUserIdAndMovieId(Long appUserId, Long movieId);
}
