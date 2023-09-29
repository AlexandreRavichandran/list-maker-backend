package com.medialistmaker.list.repository;

import com.medialistmaker.list.domain.AppUserMovieListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserMovieListRepository extends JpaRepository<AppUserMovieListItem, Long> {

    List<AppUserMovieListItem> getByAppUserIdOrderBySortingOrderAsc(Long listId);
}
